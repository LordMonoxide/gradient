package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockPebble extends Block {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(4.0d, 0.0d, 4.0d, 12.0d, 4.0d, 12.0d);

  @Nullable
  private final Metal metal;

  public BlockPebble() {
    super(Properties.create(Material.EARTH, MaterialColor.GRAY));
    this.metal = null;
  }

  public BlockPebble(final Metal metal) {
    super(Properties.create(Material.EARTH, MaterialColor.GRAY).hardnessAndResistance(0.0f).doesNotBlockMovement());
    this.metal = metal;
  }

  @Override
  public void getDrops(final BlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
    for(int i = 0; i < world.rand.nextInt(2); i++) {
      if(world.rand.nextInt(6) == 0) {
        drops.add(new ItemStack(Items.FLINT));
      } else {
        drops.add(new ItemStack(GradientItems.PEBBLE));
      }
    }

    if(this.metal != null) {
      if(world.rand.nextInt(2) == 0) {
        drops.add(new ItemStack(GradientItems.nugget(this.metal)));
      }
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final BlockState state, final BlockPos pos, final Direction face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final BlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isValidPosition(final BlockState state, final IWorldReaderBase world, final BlockPos pos) {
    final BlockState down = world.getBlockState(pos.down());

    return
      this.isAir(world.getBlockState(pos), world, pos) && (
        down.getMaterial() == Material.CLAY ||
        down.getMaterial() == Material.GRASS ||
        down.getMaterial() == Material.GROUND ||
        down.getMaterial() == Material.ICE ||
        down.getMaterial() == Material.PACKED_ICE ||
        down.getMaterial() == Material.ROCK ||
        down.getMaterial() == Material.SAND
      ) && down.isTopSolid()
    ;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos) {
    if(world.isAirBlock(pos.down())) {
      state.dropBlockAsItem(world, pos, 0);
      world.removeBlock(pos);
    }
  }
}
