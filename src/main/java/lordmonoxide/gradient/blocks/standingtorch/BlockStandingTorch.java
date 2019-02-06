package lordmonoxide.gradient.blocks.standingtorch;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockStandingTorch extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(6.0d / 16.0d, 0.0d, 6.0d / 16.0d, 10.0d / 16.0d, 1.0d, 10.0d / 16.0d);

  public BlockStandingTorch() {
    super("standing_torch", CreativeTabs.DECORATIONS, Material.WOOD);
    this.setHardness(1.0f);
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

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return AABB;
  }
}
