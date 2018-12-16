package lordmonoxide.gradient.energy;

public interface IEnergyStorage extends IEnergyNode {
  /**
   * Adds energy to the storage. Returns quantity of energy that was accepted.
   *
   * @param maxSink
   *            Maximum amount of energy to be inserted.
   * @param simulate
   *            If TRUE, the insertion will only be simulated.
   * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
   */
  float sinkEnergy(float maxSink, boolean simulate);

  /**
   * Removes energy from the storage. Returns quantity of energy that was removed.
   *
   * @param maxSource
   *            Maximum amount of energy to be extracted.
   * @param simulate
   *            If TRUE, the extraction will only be simulated.
   * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
   */
  float sourceEnergy(float maxSource, boolean simulate);

  /**
   * Returns the amount of energy currently stored.
   */
  float getEnergy();

  /**
   * Sets the amount of energy that is stored
   */
  void setEnergy(float amount);

  /**
   * Returns the maximum amount of energy that can be stored.
   */
  float getCapacity();

  float getMaxSink();
  float getMaxSource();

  default float getRequestedEnergy() {
    final float space = this.getCapacity() - this.getEnergy();

    if(!this.canSink() || space < 0.0001f) {
      return 0.0f;
    }

    return Math.min(this.getMaxSink(), space);
  }
}
