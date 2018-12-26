package lordmonoxide.gradient.blocks.kinetic.flywheel;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileFlywheel extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage energy = new KineticEnergyStorage(100.0f) {
    @Override
    public float sinkEnergy(final float maxSink, final boolean simulate) {
      System.out.println("Got " + maxSink);
      return super.sinkEnergy(maxSink, simulate);
    }
  };

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).connect(this.pos, this);
  }

  public float getEnergy() {
    return this.energy.getEnergy();
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockFlywheel.FACING);
      return facing == myFacing.rotateY() || facing == myFacing.rotateYCCW();
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockFlywheel.FACING);

      if(facing == myFacing.rotateY() || facing == myFacing.rotateYCCW()) {
        return STORAGE.cast(this.energy);
      }
    }

    return super.getCapability(capability, facing);
  }
}
