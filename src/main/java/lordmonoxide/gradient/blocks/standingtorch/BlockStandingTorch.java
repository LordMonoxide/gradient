package lordmonoxide.gradient.blocks.standingtorch;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStandingTorch extends GradientBlock {
  public BlockStandingTorch() {
    super("standing_torch", CreativeTabs.DECORATIONS, Material.WOOD);
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
    final BlockPos up = pos.up();
    return world.getBlockState(up).getBlock().isReplaceable(world, up);
  }
}
