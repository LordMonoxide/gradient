package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public class EnergyTransfer implements IEnergyTransfer {
  @Override
  public boolean canConnect(final EnumFacing side) {
    return true;
  }

  @Override
  public boolean canTransfer(final EnumFacing from, final EnumFacing to) {
    return true;
  }

  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {

  }
}
