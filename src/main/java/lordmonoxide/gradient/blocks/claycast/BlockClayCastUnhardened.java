package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockClayCastUnhardened extends Hardenable {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);

  private static final GradientCasts.PropertyCast CAST = GradientCasts.PropertyCast.create("cast");

  public BlockClayCastUnhardened() {
    super("clay_cast_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(CAST, GradientCasts.PICKAXE));
    this.setResistance(2.0f);
    this.setHardness(1.0f);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CAST);
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(CAST, GradientCasts.getCast(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(CAST).id;
  }

  @Override
  public int damageDropped(final IBlockState state) {
    return this.getMetaFromState(state);
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
    return GradientBlocks.CLAY_CAST.getDefaultState().withProperty(CAST, current.getValue(CAST));
  }

  @Override
  public int getHardeningTime(final IBlockState current) {
    return 60;
  }
}
