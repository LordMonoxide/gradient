package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockClayCrucibleUnhardened extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 1.0d - 1.0d / 16.0d, 0.75d, 1.0d - 1.0d / 16.0d);

  public BlockClayCrucibleUnhardened() {
    super("clay_crucible_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setResistance(2.0f);
    this.setHardness(1.0f);
  }

  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
}
