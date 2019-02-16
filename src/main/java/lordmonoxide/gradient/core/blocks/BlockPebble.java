package lordmonoxide.gradient.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class BlockPebble extends Block {
  public BlockPebble() {
    super(Block.Properties.create(Material.GROUND, MaterialColor.GRAY).doesNotBlockMovement());
  }

  //TODO: remove this once the forge registry is fixed
  @Override
  public Item asItem() {
    return ForgeRegistries.ITEMS.getValue(this.getRegistryName());
  }

  @Override
  public int getItemsToDropCount(final IBlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(2) + 1;
  }

  @Override
  public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos pos, final int fortune) {
    if(world.getRandom().nextInt(6) == 0) {
      return Items.FLINT;
    }

    return this;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    return VoxelShapes.create(0.25d, 0.0d, 0.25d, 0.75d, 0.25d, 0.75d);
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isValidPosition(final IBlockState state, final IWorldReaderBase world, final BlockPos pos) {
    final IBlockState down = world.getBlockState(pos.down());

    return
      super.isValidPosition(state, world, pos) && (
        down.getMaterial() == Material.CLAY ||
        down.getMaterial() == Material.GRASS ||
        down.getMaterial() == Material.GROUND ||
        down.getMaterial() == Material.ICE ||
        down.getMaterial() == Material.PACKED_ICE ||
        down.getMaterial() == Material.ROCK ||
        down.getMaterial() == Material.SAND
      ) && down.isFullCube();
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos) {
    if(world.isAirBlock(pos.down())) {
      world.destroyBlock(pos, true);
    }
  }
}
