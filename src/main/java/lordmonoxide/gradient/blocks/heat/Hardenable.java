package lordmonoxide.gradient.blocks.heat;

import net.minecraft.block.state.IBlockState;

public interface Hardenable {
  IBlockState getHardened(final IBlockState current);
  int getHardeningTime();
}
