package lordmonoxide.gradient.energy;

public class EnergyStorage implements IEnergyStorage {
  private final float capacity;
  private final float maxSink;
  private final float maxSource;
  float energy;

  public EnergyStorage(final float capacity) {
    this(capacity, capacity, capacity, 0);
  }

  public EnergyStorage(final float capacity, final float maxTransfer) {
    this(capacity, maxTransfer, maxTransfer, 0);
  }

  public EnergyStorage(final float capacity, final float maxSink, final float maxSource) {
    this(capacity, maxSink, maxSource, 0);
  }

  public EnergyStorage(final float capacity, final float maxSink, final float maxSource, final float energy) {
    this.capacity = capacity;
    this.maxSink = maxSink;
    this.maxSource = maxSource;
    this.energy = Math.max(0, Math.min(capacity, energy));
  }

  @Override
  public float sinkEnergy(final float maxSink, final boolean simulate) {
    return this.addEnergy(Math.min(this.maxSink, maxSink));
  }

  @Override
  public float sourceEnergy(final float maxSource, final boolean simulate) {
    final float energyExtracted = Math.min(this.energy, Math.min(this.maxSource, maxSource));

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
  public void setEnergy(final float amount) {
    this.energy = amount;
  }

  @Override
  public float addEnergy(final float amount) {
    final float energyReceived = Math.min(this.capacity - this.energy, amount);
    this.energy += energyReceived;
    return energyReceived;
  }

  @Override
  public float getCapacity() {
    return this.capacity;
  }

  @Override
  public float getMaxSink() {
    return this.maxSink;
  }

  @Override
  public float getMaxSource() {
    return this.maxSource;
  }

  @Override
  public boolean canSink() {
    return this.maxSource > 0;
  }

  @Override
  public boolean canSource() {
    return this.maxSink > 0;
  }
}
