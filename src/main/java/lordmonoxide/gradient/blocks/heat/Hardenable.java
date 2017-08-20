package lordmonoxide.gradient.blocks.heat;

import net.minecraft.block.state.IBlockState;

public interface Hardenable {
  IBlockState getHardened(final IBlockState current);
  int getHardeningTime(final IBlockState current);
  
  default boolean isHardened(final IBlockState current) {
    return false;
  }
}
