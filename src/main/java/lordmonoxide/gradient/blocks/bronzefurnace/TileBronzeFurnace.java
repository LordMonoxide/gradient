package lordmonoxide.gradient.blocks.bronzefurnace;

import com.google.common.collect.Lists;
import ic2.core.ref.FluidName;
import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TileBronzeFurnace extends TileEntity implements ITickable {
  private static final Fluid STEAM = FluidRegistry.getFluid(FluidName.steam.getName());
  
  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT;
  
  public static final int FIRST_INPUT_SLOT = 0;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  
  public final FluidTank tankMetal = new FluidTank(Fluid.BUCKET_VOLUME * 16);
  public final FluidTank tankSteam = new FluidTank(100);
  private final FluidHandler fluidHandler = new FluidHandler(this.tankSteam, this.tankMetal);
  
  private final MeltingMetal[] melting = new MeltingMetal[INPUT_SLOTS_COUNT];
  
  private float heat = 0;
  
  private long nextSync = 0;
  
  public boolean useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      this.sync();
      return true;
    }
    
    return false;
  }
  
  @Override
  public void update() {
    this.heatUp();
    this.meltMetal();
    this.coolDown();
    
    if(!this.getWorld().isRemote) {
      if(System.currentTimeMillis() >= this.nextSync) {
        this.nextSync = System.currentTimeMillis() + 10000L;
        this.sync();
      }
    }
  }
  
  public boolean hasHeat() {
    return this.heat != 0;
  }
  
  public float getHeat() {
    return this.heat;
  }
  
  private void addHeat(final float heat) {
    this.heat += heat;
  }
  
  private void removeHeat(final float heat) {
    this.heat = Math.max(0, this.heat - heat);
  }
  
  private ItemStack getInputStack(final int slot) {
    return this.inventory.getStackInSlot(FIRST_INPUT_SLOT + slot);
  }
  
  private void setInputStack(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_INPUT_SLOT + slot, stack);
  }
  
  boolean isMelting(final int slot) {
    return this.melting[slot] != null;
  }
  
  private MeltingMetal getMeltingMetal(final int slot) {
    return this.melting[slot];
  }
  
  @Nullable
  private FluidStack getMoltenMetal() {
    return this.tankMetal.getFluid();
  }
  
  private boolean canMelt(final GradientMetals.Meltable meltable) {
    return (this.tankMetal.getFluid() == null || this.tankMetal.getFluid().getFluid() == meltable.metal.getFluid()) && this.getHeat() >= meltable.metal.meltTemp;
  }
  
  private void meltMetal() {
    boolean update = false;
    
    for(int slot = 0; slot < INPUT_SLOTS_COUNT; slot++) {
      if(!this.isMelting(slot) && !this.getInputStack(slot).isEmpty()) {
        final GradientMetals.Meltable meltable = GradientMetals.getMeltable(this.getInputStack(slot));
        
        if(this.canMelt(meltable)) {
          this.melting[slot] = new MeltingMetal(meltable);
          update = true;
        }
      }
      
      if(this.isMelting(slot)) {
        final MeltingMetal melting = this.getMeltingMetal(slot);
        melting.tick();
        
        if(melting.isMelted()) {
          final ItemStack stack = this.getInputStack(slot);
          
          this.melting[slot] = null;
          this.setInputStack(slot, ItemStack.EMPTY);
          
          final FluidStack fluid = new FluidStack(melting.meltable.metal.getFluid(), GradientMetals.getMeltable(stack).amount);
          
          this.tankMetal.fill(fluid, true);
          
          update = true;
        }
      }
    }
    
    if(update) {
      this.sync();
    }
  }
  
  private void coolDown() {
    //this.removeHeat((float)Math.pow((this.getHeat() / 1600) + 1, 2) / 20.0f);
  }
  
  private void heatUp() {
    final FluidStack steam = this.tankSteam.drain(100, true);
    
    if(steam == null || steam.amount == 0) {
      return;
    }
  
    System.out.println("Adding heat " + steam.amount + "(" + this.heat + ")");
    
    this.addHeat(steam.amount / 20.0f);
  }
  
  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("metal", this.tankMetal.writeToNBT(new NBTTagCompound()));
    compound.setTag("steam", this.tankSteam.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.tankMetal.readFromNBT(compound.getCompoundTag("metal"));
    this.tankSteam.readFromNBT(compound.getCompoundTag("steam"));
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
      return (T)this.fluidHandler;
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
  
  public static final class MeltingMetal {
    public final GradientMetals.Meltable meltable;
    private final int totalTicks;
    private int ticks;
    
    private MeltingMetal(final GradientMetals.Meltable meltable) {
      this(meltable, 0);
    }
    
    private MeltingMetal(final GradientMetals.Meltable meltable, final int ticks) {
      this.meltable = meltable;
      this.ticks = ticks;
      this.totalTicks = Math.round(this.meltable.metal.meltTime * this.meltable.meltModifier * 20);
    }
    
    public void tick() {
      this.ticks++;
    }
    
    public boolean isMelted() {
      return this.ticks >= this.totalTicks;
    }
    
    public float meltPercent() {
      return (float)this.ticks / this.totalTicks;
    }
  }
  
  private static class FluidHandler implements IFluidHandler {
    private final IFluidHandler steam;
    private final IFluidHandler metal;
    
    public FluidHandler(final IFluidHandler steam, final IFluidHandler metal) {
      this.steam = steam;
      this.metal = metal;
    }
    
    @Override
    public IFluidTankProperties[] getTankProperties() {
      final List<IFluidTankProperties> tanks = Lists.newArrayList();
      Collections.addAll(tanks, this.steam.getTankProperties());
      Collections.addAll(tanks, this.metal.getTankProperties());
      return tanks.toArray(new IFluidTankProperties[tanks.size()]);
    }
    
    @Nullable
    private IFluidHandler getTankForFluid(@Nullable final FluidStack fluid) {
      if(fluid == null) {
        return null;
      }
      
      if(fluid.getFluid() == STEAM) {
        return this.steam;
      }
      
      return this.metal;
    }
    
    @Override
    public int fill(final FluidStack fluid, final boolean doFill) {
      final IFluidHandler tank = this.getTankForFluid(fluid);
      
      if(tank == null) {
        return 0;
      }
      
      return tank.fill(fluid, doFill);
    }
    
    @Nullable
    @Override
    public FluidStack drain(final FluidStack fluid, final boolean doDrain) {
      final IFluidHandler tank = this.getTankForFluid(fluid);
      
      if(tank == null) {
        return null;
      }
      
      return tank.drain(fluid, doDrain);
    }
    
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
      return this.steam.drain(maxDrain, doDrain);
    }
  }
}
