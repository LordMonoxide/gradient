package lordmonoxide.gradient.blocks.bronzeboiler;

import ic2.core.ref.FluidName;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;

import javax.annotation.Nullable;

public class TileBronzeBoiler extends HeatSinker {
  public static final int WATER_CAPACITY = 16;
  public static final int STEAM_CAPACITY = 32;

  private static final Fluid WATER = FluidRegistry.getFluid("water");
  private static final Fluid STEAM = FluidRegistry.getFluid(FluidName.steam.getName());

  public final FluidTank tankWater = new FluidTank(Fluid.BUCKET_VOLUME * WATER_CAPACITY);
  public final FluidTank tankSteam = new FluidTank(Fluid.BUCKET_VOLUME * STEAM_CAPACITY);
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

  @Nullable
  private IFluidHandler autoOutput;

  public TileBronzeBoiler() {
    this.tankWater.setCanDrain(false);
    this.tankSteam.setCanFill(false);

    this.tanks.addHandler(WATER, this.tankWater);
    this.tanks.addHandler(STEAM, this.tankSteam);
  }

  public void useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      final IBlockState state = world.getBlockState(pos);
      world.markAndNotifyBlock(pos, null, state, state, 2);
    }
  }

  public void updateOutput(@Nullable final IFluidHandler handler) {
    this.autoOutput = handler;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.updateOutput(FluidUtil.getFluidHandler(this.world, this.pos.up(), EnumFacing.DOWN));
  }

  @Override
  protected void tickBeforeCooldown() {

  }

  @Override
  protected void tickAfterCooldown() {
    this.boilWater();
    this.autoOutput();
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return (float)Math.pow(this.getHeat() / 1600 + 1, 2);
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private void boilWater() {
    if(this.getHeat() >= 100) {
      if(this.tankWater.getFluidAmount() > 0 && this.tankSteam.getFluidAmount() < this.tankSteam.getCapacity()) {
        final double drain = 0.000000001 * Math.pow(this.getHeat(), 3) + -0.000002257 * Math.pow(this.getHeat(), 2) + 0.003647619 * this.getHeat() + 0.011428571;

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
    compound.setTag("water", this.tankWater.writeToNBT(new NBTTagCompound()));
    compound.setTag("steam", this.tankSteam.writeToNBT(new NBTTagCompound()));

    compound.setFloat("heat", this.getHeat());

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.tankWater.readFromNBT(compound.getCompoundTag("water"));
    this.tankSteam.readFromNBT(compound.getCompoundTag("steam"));

    this.setHeat(compound.getFloat("heat"));

    super.readFromNBT(compound);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return (T)this.tanks;
    }

    return super.getCapability(capability, facing);
  }
}
