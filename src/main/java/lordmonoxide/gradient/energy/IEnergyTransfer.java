package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyTransfer extends IEnergyNode {
  /**
   * Called when energy is transferred through this object.
   *
   * <b>NOTE:</b> The actual energy transfer is done by the energy network.  This method
   * is only called to notify the transfer object that energy was transferred through it.
   *
   * @param amount The amount of energy
   * @param from   The side energy is flowing from
   * @param to     The side energy is flowing to
   */
  void transfer(final float amount, final EnumFacing from, final EnumFacing to);

  float getEnergyTransferred();
  void resetEnergyTransferred();
}
