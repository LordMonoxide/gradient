package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class TransferNode implements IEnergyTransfer {
  private float transferred;

  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {
    this.transferred += amount;
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
