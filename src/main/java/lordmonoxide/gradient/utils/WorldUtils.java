package lordmonoxide.gradient.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class WorldUtils {
  private WorldUtils() { }

  @Nullable
  public static <T extends TileEntity> T getTileEntity(final IBlockAccess world, final BlockPos pos, final Class<T> clazz) {
    final TileEntity te = world.getTileEntity(pos);
    return clazz.isInstance(te) ? clazz.cast(te) : null;
  }

  public static <T extends TileEntity> void callTileEntity(final IBlockAccess world, final BlockPos pos, final Class<T> clazz, final Consumer<T> callback) {
    final T te = getTileEntity(world, pos, clazz);

    if(te != null) {
      callback.accept(te);
    }
  }

  public static void dropItemsInTileEntity(final World world, final BlockPos pos) {
    final TileEntity te = world.getTileEntity(pos);

    if(te == null) {
      return;
    }

    final IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

    if(inv == null) {
      return;
    }

    for(int i = 0; i < inv.getSlots(); i++) {
      Block.spawnAsEntity(world, pos, inv.extractItem(i, inv.getSlotLimit(i), false));
    }
  }

  @Nullable
  public static EnumFacing areBlocksAdjacent(final BlockPos a, final BlockPos b) {
    if(a.getX() == b.getX() && a.getY() == b.getY()) {
      if(a.getZ() == b.getZ() + 1 || a.getZ() == b.getZ() - 1) {
        return getBlockFacing(a, b);
      }
    }

    if(a.getX() == b.getX() && a.getZ() == b.getZ()) {
      if(a.getY() == b.getY() + 1 || a.getY() == b.getY() - 1) {
        return getBlockFacing(a, b);
      }
    }

    if(a.getY() == b.getY() && a.getZ() == b.getZ()) {
      if(a.getX() == b.getX() + 1 || a.getX() == b.getX() - 1) {
        return getBlockFacing(a, b);
      }
    }

    return null;
  }

  /**
   * Gets the facing of <tt>origin</tt> that points towards <tt>other</tt>
   */
  public static EnumFacing getBlockFacing(final BlockPos origin, final BlockPos other) {
    return EnumFacing.getFacingFromVector(other.getX() - origin.getX(), other.getY() - origin.getY(), other.getZ() - origin.getZ());
  }

  private static final int NUM_FACING_BITS = 3;
  private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
  private static final int NUM_Z_BITS = NUM_X_BITS;
  private static final int NUM_Y_BITS = Long.SIZE - NUM_X_BITS - NUM_Z_BITS - NUM_FACING_BITS;
  private static final int X_SHIFT = NUM_Z_BITS;
  private static final int Y_SHIFT = X_SHIFT + NUM_X_BITS;
  private static final int FACING_SHIFT = Long.SIZE - NUM_FACING_BITS;
  private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
  private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
  private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

  public static long serializeBlockPosAndFacing(final BlockPos pos, final EnumFacing facing) {
    return (pos.getX() & X_MASK) << X_SHIFT | (pos.getY() & Y_MASK) << Y_SHIFT | pos.getZ() & Z_MASK | (long)facing.getIndex() << FACING_SHIFT;
  }

  public static BlockPos getBlockPosFromSerialized(final long serialized) {
    final int x = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
    final int y = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >>> 64 - NUM_Y_BITS);
    final int z = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
    return new BlockPos(x, y, z);
  }

  public static EnumFacing getFacingFromSerialized(final long serialized) {
    return EnumFacing.byIndex((int)(serialized >>> FACING_SHIFT));
  }

  public static void notifyUpdate(final World world, final BlockPos pos, final int flags) {
    final IBlockState state = world.getBlockState(pos);
    world.notifyBlockUpdate(pos, state, state, flags);
  }

  public static void notifyUpdate(final World world, final BlockPos pos) {
    notifyUpdate(world, pos, 3);
  }
}
