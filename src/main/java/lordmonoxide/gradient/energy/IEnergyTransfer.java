package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyTransfer extends IEnergyNode {
  /**
   * Can energy be transferred from side <tt>from</tt> to side <tt>to</tt>
   *
   * @param from The side energy is flowing from
   * @param to   The side energy is flowing to
   */
  boolean canTransfer(final EnumFacing from, final EnumFacing to);

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
}
