package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class EnergyTransfer implements IEnergyTransfer {
  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {

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
