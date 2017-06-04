package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileFirePit extends TileEntity implements ITickable {
  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int OUTPUT_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;
  
  public static final int FIRST_FUEL_SLOT = 0;
  public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
  public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;
  
  private ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  
  private BurningFuel[] fuels = new BurningFuel[FUEL_SLOTS_COUNT];
  
  private float heat;
  private int lastLight;
  private long nextSync;
  
  public float getHeat() {
    return this.heat;
  }
  
  public boolean isBurning() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isBurning(int slot) {
    return this.fuels[slot] != null;
  }
  
  public BurningFuel getBurningFuel(int slot) {
    return fuels[slot];
  }
  
  public int getLightLevel() {
    if(this.getHeat() == 0) {
      return 0;
    }
    
    return Math.min((int)(this.getHeat() / 1000 * 11) + 4, 15);
  }
  
  public void light() {
    if(this.isBurning()) {
      return;
    }
    
    this.heat = Math.max(50, this.heat);
    this.sync();
  }
  
  protected void sync() {
    if(!this.getWorld().isRemote) {
      IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
    }
  }
  
  @Override
  public void update() {
    if(this.heat == 0) {
      return;
    }
    
    this.igniteFuel();
    this.coolDown();
    this.heatUp();
    //this.cook();
    this.updateLight();
  
    if(this.getWorld().isRemote) {
      this.generateParticles();
      this.playSounds();
    } else {
      if(Minecraft.getSystemTime() >= this.nextSync) {
        this.nextSync = Minecraft.getSystemTime() + 10000;
        this.sync();
      }
    }
  }
  
  private void igniteFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(!this.isBurning(i) && this.getFuelSlot(i) != null) {
        GradientFuel.Fuel fuel = GradientFuel.instance.get(this.getFuelSlot(i));
        
        if(this.canIgnite(fuel)) {
          this.fuels[i] = new BurningFuel(fuel);
        }
      }
    }
  }
  
  private void coolDown() {
    float loss = this.calculateHeatLoss() / 20f;
    this.heat = Math.max(this.getHeat() - loss, 0);
  }
  
  private void heatUp() {
    float temperatureChange = 0;
    
    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(this.isBurning(slot)) {
        BurningFuel fuel = this.fuels[slot];
        
        if(fuel.isDepleted()) {
          this.fuels[slot] = null;
          this.setFuelSlot(slot, null);
        }
        
        if(fuel.fuel.burnTemp > this.heat) {
          temperatureChange += fuel.fuel.heatPerTick;
        }
      }
    }
    
    this.heat += temperatureChange;
  }
  
  private void updateLight() {
    if(this.lastLight != this.getLightLevel()) {
      this.getWorld().markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.getWorld().checkLight(this.pos);
      
      this.lastLight = this.getLightLevel();
    }
  }
  
  private void generateParticles() {
    if(this.heat > 0) {
      if(this.isBurning()) { // Fire
        double radius = this.getWorld().rand.nextDouble() * 0.25;
        double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;
        
        double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);
        
        this.getWorld().spawnParticle(EnumParticleTypes.FLAME, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
      }
      
      { // Smoke
        double radius = this.getWorld().rand.nextDouble() * 0.35;
        double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;
        
        double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);
        
        this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
      }
    }
  }
  
  private void playSounds() {
    if(this.getHeat() > 0) {
      if(this.getWorld().rand.nextInt(40) == 0) {
        this.getWorld().playSound(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.8f + this.getWorld().rand.nextFloat(), this.getWorld().rand.nextFloat() * 0.7f + 0.3f, false);
      }
    }
  }
  
  protected float calculateHeatLoss() {
    return (float)Math.pow((this.getHeat() / 500) + 1, 2) / 1.5f;
  }
  
  private ItemStack getFuelSlot(int slot) {
    return this.inventory.getStackInSlot(slot);
  }
  
  private void setFuelSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }
  
  private boolean canIgnite(GradientFuel.Fuel fuel) {
    return this.heat >= fuel.ignitionTemp;
  }
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setFloat("heat", this.heat);
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();
    
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.heat = compound.getFloat("heat");
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
  }
  
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }
  
  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }
  
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
  
  public class BurningFuel {
    public final GradientFuel.Fuel fuel;
    public final long burnStart;
    public final long burnUntil;
    
    private BurningFuel(GradientFuel.Fuel fuel) {
      this.fuel = fuel;
      this.burnStart = Minecraft.getSystemTime();
      this.burnUntil = this.burnStart + fuel.duration * 1000;
    }
    
    public boolean isDepleted() {
      return Minecraft.getSystemTime() >= this.burnUntil;
    }
    
    public float burnPercent() {
      return (float)(Minecraft.getSystemTime() - this.burnStart) / (this.burnUntil - this.burnStart);
    }
  }
}
