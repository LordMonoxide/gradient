package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Predicate;

public class StorageNode implements IEnergyStorage {
  private final Predicate<EnumFacing> sinkSides;
  private final Predicate<EnumFacing> sourceSides;

  private final float capacity;
  private final float maxReceive;
  private final float maxExtract;
  private float energy;

  public StorageNode() {
    this(0.0f, 0.0f, 0.0f, 0.0f);
  }

  public StorageNode(final float capacity, final float maxReceive, final float maxExtract, final float energy) {
    this(capacity, maxReceive, maxExtract, energy, facing -> true, facing -> true);
  }

  public StorageNode(final float capacity, final float maxReceive, final float maxExtract, final float energy, final EnumFacing[] sinkSides, final EnumFacing[] sourceSides) {
    this(capacity, maxReceive, maxExtract, energy, facing -> ArrayUtils.contains(sinkSides, facing), facing -> ArrayUtils.contains(sourceSides, facing));
  }

  public StorageNode(final float capacity, final float maxReceive, final float maxExtract, final float energy, final Predicate<EnumFacing> sinkSides, final Predicate<EnumFacing> sourceSides) {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
    this.energy = energy;
    this.sinkSides   = sinkSides;
    this.sourceSides = sourceSides;
  }

  @Override
  public boolean canSink(final EnumFacing side) {
    return this.sinkSides.test(side);
  }

  @Override
  public boolean canSource(final EnumFacing side) {
    return this.sourceSides.test(side);
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
