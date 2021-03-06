package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class TransferNode implements IEnergyTransfer {
  private float transferred;

  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {
    this.transferred += amount;
  }

  @Override
  public float getEnergyTransferred() {
    return this.transferred;
  }

  @Override
  public void resetEnergyTransferred() {
    this.transferred = 0.0f;
  }

  public float getTransferred() {
    return this.transferred;
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
