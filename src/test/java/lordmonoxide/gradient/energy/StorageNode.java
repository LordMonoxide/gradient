package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class StorageNode implements IEnergyStorage {
  @Override
  public boolean canConnect(final EnumFacing side) {
    return true;
  }

  @Override
  public float receiveEnergy(final float maxReceive, final boolean simulate) {
    return 0;
  }

  @Override
  public float extractEnergy(final float maxExtract, final boolean simulate) {
    return 0;
  }

  @Override
  public float getEnergyStored() {
    return 0;
  }

  @Override
  public float getMaxEnergyStored() {
    return 0;
  }

  @Override
  public boolean canExtract() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return true;
  }
}
