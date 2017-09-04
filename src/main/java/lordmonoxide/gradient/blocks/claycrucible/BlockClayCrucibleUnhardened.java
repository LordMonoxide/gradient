package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockClayCrucibleUnhardened extends GradientBlock implements Hardenable {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1d / 16d, 0.0d, 1d / 16d, 1d - 1d / 16d, 0.75d, 1d - 1d / 16d);
  
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
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return GradientBlocks.CLAY_CRUCIBLE.getDefaultState();
  }
  
  @Override
  public int getHardeningTime(final IBlockState current) {
    return 180;
  }
}
