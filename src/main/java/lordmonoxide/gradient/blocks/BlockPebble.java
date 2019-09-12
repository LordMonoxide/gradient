package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPebble extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25d, 0.0d, 0.25d, 0.75d, 0.25d, 0.75d);

  @Nullable
  private final Metal metal;

  public BlockPebble() {
    super("pebble", CreativeTabs.MATERIALS, Material.GROUND, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
    this.metal = null;
  }

  public BlockPebble(final Metal metal) {
    super("pebble." + metal.name, CreativeTabs.MATERIALS, Material.GROUND, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
    this.metal = metal;
    this.setTranslationKey("pebble");
  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    final Random rand = world instanceof World ? ((World)world).rand : RANDOM;

    for(int i = 0; i < rand.nextInt(2); i++) {
      if(rand.nextInt(6) == 0) {
        drops.add(new ItemStack(Items.FLINT));
      } else {
        drops.add(new ItemStack(GradientItems.PEBBLE));
      }
    }

    if(this.metal != null) {
      if(rand.nextInt(2) == 0) {
        drops.add(new ItemStack(GradientItems.nugget(this.metal)));
      }
    }
  }

  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for render
   */
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }

  @Override
  @Nullable
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess world, final BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
    final IBlockState down = world.getBlockState(pos.down());

    return
      super.canPlaceBlockAt(world, pos) && (
        down.getMaterial() == Material.CLAY ||
        down.getMaterial() == Material.GRASS ||
        down.getMaterial() == Material.GROUND ||
        down.getMaterial() == Material.ICE ||
        down.getMaterial() == Material.PACKED_ICE ||
        down.getMaterial() == Material.ROCK ||
        down.getMaterial() == Material.SAND
      ) && down.isFullBlock()
    ;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos fromPos) {
    if(world.isAirBlock(pos.down())) {
      this.dropBlockAsItem(world, pos, state, 0);
      world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
  }
}
