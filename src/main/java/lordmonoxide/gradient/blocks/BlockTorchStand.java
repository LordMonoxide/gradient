package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockTorchStand extends Block {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(6.0d, 0.0d, 6.0d, 10.0d, 16.0d, 10.0d);

  public BlockTorchStand() {
    super(Properties.create(Material.WOOD).hardnessAndResistance(1.0f));
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
    return SHAPE;
  }
}
