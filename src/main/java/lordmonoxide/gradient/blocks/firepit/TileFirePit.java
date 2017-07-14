package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientFood;
import lordmonoxide.gradient.GradientFuel;
import lordmonoxide.gradient.blocks.Hardenable;
import lordmonoxide.gradient.blocks.heat.HeatProducer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TileFirePit extends HeatProducer {
  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int OUTPUT_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;
  
  public static final int FIRST_FUEL_SLOT = 0;
  public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
  public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  
  private final BurningFuel[] fuels = new BurningFuel[FUEL_SLOTS_COUNT];
  private final CookingFood[] foods = new CookingFood[INPUT_SLOTS_COUNT];
  
  private final Map<BlockPos, Hardening> hardenables = new HashMap<>();
  
  private boolean firstTick = true;
  
  private boolean hasFurnace;
  
  private int lastLight;
  private long nextSync;
  
  public boolean hasFurnace() {
    return this.hasFurnace;
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
    return this.fuels[slot];
  }
  
  public boolean isCooking() {
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(this.isCooking(i)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isCooking(int slot) {
    return this.foods[slot] != null;
  }
  
  public CookingFood getCookingFood(int slot) {
    return this.foods[slot];
  }
  
  public int getLightLevel() {
    if(this.getHeat() == 0) {
      return 0;
    }
    
    return Math.min(
      !this.hasFurnace ?
        (int)(this.getHeat() / 800 * 11) + 4 :
        (int)(this.getHeat() / 1000 * 9) + 2,
      15
    );
  }
  
  public void light() {
    if(this.isBurning()) {
      return;
    }
    
    this.setHeat(Math.max(50, this.getHeat()));
    this.sync();
  }
  
  public void attachFurnace() {
    this.hasFurnace = true;
    this.sync();
  }
  
  protected void sync() {
    if(!this.getWorld().isRemote) {
      IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
    }
  }
  
  public void updateHardenable(BlockPos pos) {
    if(this.world.isRemote) {
      return;
    }
    
    Block block = this.getWorld().getBlockState(pos).getBlock();
    
    if(!(block instanceof Hardenable)) {
      this.hardenables.remove(pos);
      return;
    }
    
    this.hardenables.put(pos, new Hardening((Hardenable)block, pos));
  }
  
  private void findSurroundingHardenables() {
    BlockPos north = this.pos.north();
    BlockPos south = this.pos.south();
    
    this.updateHardenable(north.east());
    this.updateHardenable(north);
    this.updateHardenable(north.west());
    this.updateHardenable(this.pos.east());
    this.updateHardenable(this.pos.west());
    this.updateHardenable(south.east());
    this.updateHardenable(south);
    this.updateHardenable(south.west());
  }
  
  @Override
  protected void tickBeforeCooldown() {
    this.igniteFuel();
  }
  
  @Override
  protected void tickAfterCooldown() {
    super.tickAfterCooldown();
    
    this.cook();
    this.updateLight();
    
    if(this.getWorld().isRemote) {
      this.generateParticles();
      this.playSounds();
    } else {
      if(this.firstTick) {
        this.findSurroundingHardenables();
        this.firstTick = false;
      }
      
      this.hardenHardenables();
      
      if(Minecraft.getSystemTime() >= this.nextSync) {
        this.nextSync = Minecraft.getSystemTime() + 10000;
        this.sync();
      }
    }
  }
  
  private void igniteFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(!this.isBurning(i) && !this.getFuelSlot(i).isEmpty()) {
        GradientFuel.Fuel fuel = GradientFuel.instance.get(this.getFuelSlot(i));
        
        if(this.canIgnite(fuel)) {
          this.fuels[i] = new BurningFuel(fuel);
        }
      }
    }
  }
  
  private void cook() {
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(!this.isCooking(i) && !this.getFoodSlot(i).isEmpty()) {
        GradientFood.Food food = GradientFood.instance.get(this.getFoodSlot(i));
      
        if(this.canCook(food)) {
          this.foods[i] = new CookingFood(food);
        }
      }
      
      if(this.isCooking(i)) {
        CookingFood food = this.getCookingFood(i);
        
        if(food.isCooked()) {
          this.foods[i] = null;
          this.setFoodSlot(i, ItemStack.EMPTY);
          this.setCookedSlot(i, food.food.cooked);
        }
      }
    }
  }
  
  private void hardenHardenables() {
    if(this.hardenables.isEmpty()) {
      return;
    }
    
    this.hardenables.keySet().removeIf(pos -> !(this.getWorld().getBlockState(pos).getBlock() instanceof Hardenable));
    this.hardenables.values().stream()
      .filter(Hardening::isHardened)
      .collect(Collectors.toList()) // Gotta decouple here to avoid concurrent modification exceptions
      .forEach(hardenable -> this.getWorld().setBlockState(hardenable.pos, hardenable.block.getHardened().getDefaultState()));
  }
  
  private void updateLight() {
    if(this.lastLight != this.getLightLevel()) {
      this.getWorld().markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.getWorld().checkLight(this.pos);
      
      this.lastLight = this.getLightLevel();
    }
  }
  
  private void generateParticles() {
    if(this.hasHeat()) {
      if(this.isBurning()) { // Fire
        double radius = this.getWorld().rand.nextDouble() * 0.25d;
        double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;
        
        double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);
        
        this.getWorld().spawnParticle(EnumParticleTypes.FLAME, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
      }
      
      { // Smoke
        double radius = this.getWorld().rand.nextDouble() * 0.35d;
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
  
  @Override
  protected float calculateHeatGain() {
    float temperatureChange = 0;
    
    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(this.isBurning(slot)) {
        BurningFuel fuel = this.fuels[slot];
        
        if(fuel.isDepleted()) {
          this.fuels[slot] = null;
          this.setFuelSlot(slot, ItemStack.EMPTY);
        }
        
        if(fuel.fuel.burnTemp > this.getHeat()) {
          temperatureChange += fuel.fuel.heatPerSec;
        }
      }
    }
    
    return temperatureChange;
  }
  
  @Override
  protected float calculateHeatLoss() {
    return !this.hasFurnace ?
      (float)Math.pow((this.getHeat() / 500) + 1, 2) / 1.5f :
      (float)Math.pow((this.getHeat() / 1600) + 1, 2);
  }
  
  private ItemStack getFuelSlot(int slot) {
    return this.inventory.getStackInSlot(FIRST_FUEL_SLOT + slot);
  }
  
  private void setFuelSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }
  
  private boolean canIgnite(GradientFuel.Fuel fuel) {
    return this.getHeat() >= fuel.ignitionTemp;
  }
  
  private ItemStack getFoodSlot(int slot) {
    return this.inventory.getStackInSlot(FIRST_INPUT_SLOT + slot);
  }
  
  private void setFoodSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_INPUT_SLOT + slot, stack);
  }
  
  private boolean canCook(GradientFood.Food food) {
    return this.getHeat() >= food.cookTemp;
  }
  
  private void setCookedSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_OUTPUT_SLOT + slot, stack);
  }
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setBoolean("hasFurnace", this.hasFurnace);
    compound.setFloat("heat", this.getHeat());
    
    NBTTagList fuels = new NBTTagList();
    NBTTagList foods = new NBTTagList();
    
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        BurningFuel fuel = this.getBurningFuel(i);
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", fuel.burnStart - Minecraft.getSystemTime());
        tag.setLong("until", fuel.burnUntil - Minecraft.getSystemTime());
        fuels.appendTag(tag);
      }
    }
    
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(this.isCooking(i)) {
        CookingFood food = this.getCookingFood(i);
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", food.cookStart - Minecraft.getSystemTime());
        tag.setLong("until", food.cookUntil - Minecraft.getSystemTime());
        foods.appendTag(tag);
      }
    }
    
    compound.setTag("fuel", fuels);
    compound.setTag("food", foods);
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();
    
    Arrays.fill(this.fuels, null);
    Arrays.fill(this.foods, null);
    
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.hasFurnace = compound.getBoolean("hasFurnace");
    this.setHeat(compound.getFloat("heat"));
    
    NBTTagList fuels = compound.getTagList("fuel", Constants.NBT.TAG_COMPOUND);
    NBTTagList foods = compound.getTagList("food", Constants.NBT.TAG_COMPOUND);
    
    for(int i = 0; i < fuels.tagCount(); i++) {
      NBTTagCompound tag = fuels.getCompoundTagAt(i);
      
      int slot = tag.getInteger("slot");
      
      if(slot < FUEL_SLOTS_COUNT) {
        this.fuels[slot] = new BurningFuel(GradientFuel.instance.get(this.getFuelSlot(slot)));
        this.fuels[slot].burnStart = tag.getLong("start") + Minecraft.getSystemTime();
        this.fuels[slot].burnUntil = tag.getLong("until") + Minecraft.getSystemTime();
      }
    }
    
    for(int i = 0; i < foods.tagCount(); i++) {
      NBTTagCompound tag = foods.getCompoundTagAt(i);
      
      int slot = tag.getInteger("slot");
      
      if(slot < INPUT_SLOTS_COUNT) {
        this.foods[slot] = new CookingFood(GradientFood.instance.get(this.getFoodSlot(slot)));
        this.foods[slot].cookStart = tag.getLong("start") + Minecraft.getSystemTime();
        this.foods[slot].cookUntil = tag.getLong("until") + Minecraft.getSystemTime();
      }
    }
    
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory : super.getCapability(capability, facing);
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
  
  public static final class BurningFuel {
    public final GradientFuel.Fuel fuel;
    private long burnStart;
    private long burnUntil;
    
    private BurningFuel(GradientFuel.Fuel fuel) {
      this.fuel = fuel;
      this.burnStart = Minecraft.getSystemTime();
      this.burnUntil = this.burnStart + fuel.duration * 1000L;
    }
    
    public boolean isDepleted() {
      return Minecraft.getSystemTime() >= this.burnUntil;
    }
    
    public float burnPercent() {
      return (float)(Minecraft.getSystemTime() - this.burnStart) / (this.burnUntil - this.burnStart);
    }
  }
  
  public static final class CookingFood {
    public final GradientFood.Food food;
    private long cookStart;
    private long cookUntil;
    
    private CookingFood(GradientFood.Food food) {
      this.food = food;
      this.cookStart = Minecraft.getSystemTime();
      this.cookUntil = this.cookStart + food.duration * 1000L;
    }
    
    public boolean isCooked() {
      return Minecraft.getSystemTime() >= this.cookUntil;
    }
    
    public float cookPercent() {
      return (float)(Minecraft.getSystemTime() - this.cookStart) / (this.cookUntil - this.cookStart);
    }
  }
  
  public static final class Hardening {
    public final Hardenable block;
    public final BlockPos pos;
    private long hardenStart;
    private long hardenUntil;
    
    private Hardening(Hardenable block, BlockPos pos) {
      this.block = block;
      this.pos = pos;
      this.hardenStart = Minecraft.getSystemTime();
      this.hardenUntil = this.hardenStart + block.getHardeningTime() * 1000L;
    }
    
    public boolean isHardened() {
      return Minecraft.getSystemTime() >= this.hardenUntil;
    }
  }
}
