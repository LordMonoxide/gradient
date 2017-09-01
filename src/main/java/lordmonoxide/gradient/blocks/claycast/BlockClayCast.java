package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.GradientCasts;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockClayCast extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);
  
  private static final GradientCasts.PropertyCast CAST = GradientCasts.PropertyCast.create("cast");
  
  public BlockClayCast() {
    super("clay_cast", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setDefaultState(this.blockState.getBaseState().withProperty(CAST, GradientCasts.PICKAXE));
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CAST);
  }
  
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(CAST, GradientCasts.CASTS.get(meta));
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
}
