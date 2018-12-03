package lordmonoxide.gradient.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public final class BlockPosUtils {
  private BlockPosUtils() { }

  public static boolean areBlocksAdjacent(final BlockPos a, final BlockPos b) {
    if(a.getX() == b.getX() && a.getY() == b.getY()) {
      if(a.getZ() == b.getZ() + 1 || a.getZ() == b.getZ() - 1) {
        return true;
      }
    }

    if(a.getX() == b.getX() && a.getZ() == b.getZ()) {
      if(a.getY() == b.getY() + 1 || a.getY() == b.getY() - 1) {
        return true;
      }
    }

    if(a.getY() == b.getY() && a.getZ() == b.getZ()) {
      if(a.getX() == b.getX() + 1 || a.getX() == b.getX() - 1) {
        return true;
      }
    }

    return false;
  }

  public static EnumFacing getBlockFacing(final BlockPos origin, final BlockPos other) {
    return EnumFacing.getFacingFromVector(other.getX() - origin.getX(), other.getY() - origin.getY(), other.getZ() - origin.getZ());
  }
}
