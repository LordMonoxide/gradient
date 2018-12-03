package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class EnergyStorage implements IEnergyStorage {
  private final float capacity;
  private final float maxReceive;
  private final float maxExtract;
  float energy;

  public EnergyStorage(final float capacity) {
    this(capacity, capacity, capacity, 0);
  }

  public EnergyStorage(final float capacity, final float maxTransfer) {
    this(capacity, maxTransfer, maxTransfer, 0);
  }

  public EnergyStorage(final float capacity, final float maxReceive, final float maxExtract) {
    this(capacity, maxReceive, maxExtract, 0);
  }

  public EnergyStorage(final float capacity, final float maxReceive, final float maxExtract, final float energy) {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
    this.energy = Math.max(0, Math.min(capacity, energy));
  }

  @Override
  public float receiveEnergy(final float maxReceive, final boolean simulate) {
    final float energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));

    if(!simulate) {
      this.energy += energyReceived;
    }

    return energyReceived;
  }

  @Override
  public float extractEnergy(final float maxExtract, final boolean simulate) {
    final float energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));

    if(!simulate) {
      this.energy -= energyExtracted;
    }

    return energyExtracted;
  }

  @Override
  public float getEnergy() {
    return this.energy;
  }

  @Override
  public float getCapacity() {
    return this.capacity;
  }

  @Override
  public boolean canSink(final EnumFacing side) {
    return this.maxExtract > 0;
  }

  @Override
  public boolean canSource(final EnumFacing side) {
    return this.maxReceive > 0;
  }
}
