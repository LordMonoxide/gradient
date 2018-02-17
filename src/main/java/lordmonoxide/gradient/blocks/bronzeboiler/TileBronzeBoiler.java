package lordmonoxide.gradient.blocks.bronzeboiler;

import ic2.core.ref.FluidName;
import lordmonoxide.gradient.GradientFuel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileBronzeBoiler extends TileEntity implements ITickable {
  private static final Fluid WATER = FluidRegistry.getFluid("water");
  private static final Fluid STEAM = FluidRegistry.getFluid(FluidName.steam.getName());
  
  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT;
  
  public static final int FIRST_FUEL_SLOT = 0;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  public final FluidTank tankWater = new FluidTank(Fluid.BUCKET_VOLUME * 16);
  public final FluidTank tankSteam = new FluidTank(Fluid.BUCKET_VOLUME * 32);
  private final FluidHandlerFluidMap tanks = new FluidHandlerFluidMap() {
    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
      final int amount = super.fill(resource, doFill);
      
      if(amount != 0) {
        TileBronzeBoiler.this.sync();
      }
      
      return amount;
    }
  };
  
  private final GradientFuel.BurningFuel[] fuels = new GradientFuel.BurningFuel[FUEL_SLOTS_COUNT];
  
  private long nextSync = 0;
  
  private float heat = 0.0f;
  
  @Nullable
  private IFluidHandler autoOutput = null;
  
  public TileBronzeBoiler() {
    this.tankWater.setCanDrain(false);
    this.tankSteam.setCanFill(false);
    
    this.tanks.addHandler(WATER, this.tankWater);
    this.tanks.addHandler(STEAM, this.tankSteam);
  }
  
  private boolean hasHeat() {
    return this.heat != 0;
  }
  
  public float getHeat() {
    return this.heat;
  }
  
  private void setHeat(final float heat) {
    this.heat = heat;
  }
  
  private void addHeat(final float heat) {
    this.heat += heat;
  }
  
  private void removeHeat(final float heat) {
    this.heat = Math.max(0, this.heat - heat);
  }
  
  private boolean isBurning() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isBurning(final int slot) {
    return this.fuels[slot] != null;
  }
  
  public GradientFuel.BurningFuel getBurningFuel(final int slot) {
    return this.fuels[slot];
  }
  
  private ItemStack getFuelSlot(final int slot) {
    return this.inventory.getStackInSlot(FIRST_FUEL_SLOT + slot);
  }
  
  private void setFuelSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }
  
  private boolean hasFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.getFuelSlot(i) != ItemStack.EMPTY) {
        return true;
      }
    }
    
    return false;
  }
  
  private boolean canIgnite(final GradientFuel.Fuel fuel) {
    return this.getHeat() >= fuel.ignitionTemp;
  }
  
  public void light() {
    if(this.isBurning() || !this.hasFuel()) {
      return;
    }
  
    this.setHeat(Math.max(300, this.getHeat()));
    this.sync();
  }
  
  public void useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      this.sync();
    }
  }
  
  public void updateOutput(@Nullable final IFluidHandler handler) {
    this.autoOutput = handler;
  }
  
  @Override
  public void onLoad() {
    this.updateOutput(FluidUtil.getFluidHandler(this.world, this.pos.up(), EnumFacing.DOWN));
  }
  
  @Override
  public void update() {
    if(!this.hasHeat()) {
      return;
    }
    
    this.coolDown();
    this.heatUp();
    this.igniteFuel();
    this.boilWater();
    this.autoOutput();
    
    if(!this.getWorld().isRemote) {
      if(System.currentTimeMillis() >= this.nextSync) {
        this.nextSync = System.currentTimeMillis() + 10000L;
        this.sync();
      }
    }
  }
  
  private void coolDown() {
    this.removeHeat(this.calculateHeatLoss() / 20.0f);
  }
  
  private void heatUp() {
    this.addHeat(this.calculateHeatGain() / 20.0f);
  }
  
  private float calculateHeatLoss() {
    return (float)Math.pow((this.getHeat() / 1600) + 1, 2);
  }
  
  private float calculateHeatGain() {
    float temperatureChange = 0;
    
    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(this.isBurning(slot)) {
        final GradientFuel.BurningFuel fuel = this.fuels[slot];
        
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
  
  private void igniteFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(!this.isBurning(i) && !this.getFuelSlot(i).isEmpty()) {
        final GradientFuel.Fuel fuel = GradientFuel.get(this.getFuelSlot(i));
        
        if(this.canIgnite(fuel)) {
          this.fuels[i] = new GradientFuel.BurningFuel(fuel);
        }
      }
    }
  }
  
  private void boilWater() {
    if(this.getHeat() >= 100) {
      if(this.tankWater.getFluidAmount() > 0 && this.tankSteam.getFluidAmount() < this.tankSteam.getCapacity()) {
        double drain = 0.000000001 * Math.pow(this.getHeat(), 3) + -0.000002257 * Math.pow(this.heat, 2) + 0.003647619 * this.getHeat() + 0.011428571;

        this.tankWater.setCanDrain(true);
        final FluidStack water = this.tankWater.drain(Math.max(1, (int)Math.round(drain)), true);
        this.tankWater.setCanDrain(false);

        final FluidStack steam = new FluidStack(STEAM, water.amount);
        
        this.tankSteam.setCanFill(true);
        this.tankSteam.fill(steam, true);
        this.tankSteam.setCanFill(false);
        
        this.markDirty();
      }
    }
  }
  
  private void autoOutput() {
    if(this.autoOutput != null) {
      FluidUtil.tryFluidTransfer(this.autoOutput, this.tankSteam, 20, true);
    }
  }
  
  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("water", this.tankWater.writeToNBT(new NBTTagCompound()));
    compound.setTag("steam", this.tankSteam.writeToNBT(new NBTTagCompound()));
    
    compound.setFloat("heat", this.getHeat());
    
    final NBTTagList fuels = new NBTTagList();
    
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        final GradientFuel.BurningFuel fuel = this.getBurningFuel(i);
        
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", fuel.burnStart - System.currentTimeMillis());
        tag.setLong("until", fuel.burnUntil - System.currentTimeMillis());
        fuels.appendTag(tag);
      }
    }
    
    compound.setTag("fuel", fuels);
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    Arrays.fill(this.fuels, null);
    
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.tankWater.readFromNBT(compound.getCompoundTag("water"));
    this.tankSteam.readFromNBT(compound.getCompoundTag("steam"));
    
    this.setHeat(compound.getFloat("heat"));
    
    final NBTTagList fuels = compound.getTagList("fuel", Constants.NBT.TAG_COMPOUND);
    
    for(int i = 0; i < fuels.tagCount(); i++) {
      final NBTTagCompound tag = fuels.getCompoundTagAt(i);
      
      final int slot = tag.getInteger("slot");
      
      if(slot < FUEL_SLOTS_COUNT) {
        this.fuels[slot] = new GradientFuel.BurningFuel(
          GradientFuel.get(this.getFuelSlot(slot)),
          tag.getLong("start") + System.currentTimeMillis(),
          tag.getLong("until") + System.currentTimeMillis()
        );
      }
    }
    
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
      capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return (T)this.tanks;
    }
    
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory :
      super.getCapability(capability, facing);
  }
  
  private void sync() {
    if(!this.getWorld().isRemote) {
      final IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
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
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}
