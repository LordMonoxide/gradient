package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockTorchStand extends Block {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(6.0d, 0.0d, 6.0d, 10.0d, 16.0d, 10.0d);

  public BlockTorchStand() {
    super(Properties.create(Material.WOOD).hardnessAndResistance(1.0f));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(face == EnumFacing.UP || face == EnumFacing.DOWN) {
      return BlockFaceShape.CENTER;
    }

    return BlockFaceShape.UNDEFINED;
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
  public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    return SHAPE;
  }
}
