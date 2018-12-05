package lordmonoxide.gradient.energy;

public class StorageNode implements IEnergyStorage {
  private final float capacity;
  private final float maxReceive;
  private final float maxExtract;
  private float energy;

  public StorageNode() {
    this(0.0f, 1.0f, 1.0f, 0.0f);
  }

  public StorageNode(final float capacity, final float maxReceive, final float maxExtract, final float energy) {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
    this.energy = energy;
  }

  @Override
  public boolean canSink() {
    return this.maxReceive != 0.0f;
  }

  @Override
  public boolean canSource() {
    return this.maxExtract != 0.0f;
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

  public StorageNode setEnergy(final float energy) {
    this.energy = energy;
    return this;
  }
}
