package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class EnergyTransfer implements IEnergyTransfer {
  private float energy;

  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {
    this.energy += amount;
  }

  @Override
  public float getEnergyTransferred() {
    return this.energy;
  }

  @Override
  public void resetEnergyTransferred() {
    this.energy = 0.0f;
  }

  @Override
  public boolean canSink() {
    return true;
  }

  @Override
  public boolean canSource() {
    return true;
  }
}
