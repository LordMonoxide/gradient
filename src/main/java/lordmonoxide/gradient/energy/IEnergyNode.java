package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;

public interface IEnergyNode {
  /**
   * Can this node sink power on this side?
   */
  boolean canSink(final EnumFacing side);

  /**
   * Can this node source power on this side?
   */
  boolean canSource(final EnumFacing side);
}
