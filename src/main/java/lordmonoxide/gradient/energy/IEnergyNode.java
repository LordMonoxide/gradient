package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyNode {
  /**
   * Can this node be connected to on this side?
   *
   * @param side The side the other node is attempting to connect to
   */
  boolean canConnect(final EnumFacing side);
}
