package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;

import javax.annotation.Nullable;

public class TileBronzeBoiler extends HeatSinker {
  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final int WATER_CAPACITY = 16;
  public static final int STEAM_CAPACITY = 32;

  private final Fluid WATER = FluidRegistry.getFluid("water");
  private final Fluid STEAM = FluidRegistry.getFluid("steam");

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

  private int lastWaterLevel;
  private int lastSteamLevel;

  public TileBronzeBoiler() {
    this.tankWater.setCanDrain(false);
    this.tankSteam.setCanFill(false);

    this.tanks.addHandler(this.WATER, this.tankWater);
    this.tanks.addHandler(this.STEAM, this.tankSteam);
  }

  public void useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      final IBlockState state = world.getBlockState(pos);
      world.markAndNotifyBlock(pos, null, state, state, 2);
    }
  }

  public void updateOutput(@Nullable final BlockPos pos) {
    this.autoOutput = FluidUtil.getFluidHandler(this.world, pos, EnumFacing.DOWN);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.updateOutput(this.pos.up());
  }

  @Override
  protected void tickBeforeCooldown(final float tickScale) {

  }

  @Override
  protected void tickAfterCooldown(final float tickScale) {
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

        final FluidStack steam = new FluidStack(this.STEAM, water.amount);

        this.tankSteam.setCanFill(true);
        this.tankSteam.fill(steam, true);
        this.tankSteam.setCanFill(false);

        if(this.lastWaterLevel != this.getWaterLevel() || this.lastSteamLevel != this.getSteamLevel()) {
          this.lastWaterLevel = this.getWaterLevel();
          this.lastSteamLevel = this.getSteamLevel();

          final IBlockState state = this.world.getBlockState(this.pos);
          this.world.markAndNotifyBlock(this.pos, null, state, state, 2);
        }

        this.markDirty();
      }
    }
  }

  private void autoOutput() {
    if(this.autoOutput != null) {
      FluidUtil.tryFluidTransfer(this.autoOutput, this.tankSteam, 20, true);
    }
  }

  public int getWaterLevel() {
    return (int)Math.ceil((float)this.tankWater.getFluidAmount() / Fluid.BUCKET_VOLUME);
  }

  public int getSteamLevel() {
    return (int)Math.ceil((float)this.tankSteam.getFluidAmount() / Fluid.BUCKET_VOLUME);
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
      capability == FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == FLUID_HANDLER_CAPABILITY) {
      return FLUID_HANDLER_CAPABILITY.cast(this.tanks);
    }

    return super.getCapability(capability, facing);
  }
}
