package lordmonoxide.gradient.blocks.claybucket;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayBucket extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0d / 16.0d, 0.0d, 3.0d / 16.0d, 1.0d - 3.0d / 16.0d, 0.5d, 1.0d - 3.0d / 16.0d);

  public static BlockClayBucket hardened() {
    return new BlockClayBucket(true);
  }

  public static BlockClayBucket unhardened() {
    return new BlockClayBucket(false);
  }

  private final boolean hardened;

  protected BlockClayBucket(final boolean hardened) {
    super("clay_bucket" + '.' + (hardened ? "hardened" : "unhardened"), CreativeTabs.TOOLS, hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY);
    this.setResistance(hardened ? 5.0f : 2.0f);
    this.setHardness(1.0f);
    this.hardened = hardened;
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    if(!this.hardened) {
      tooltip.add(I18n.format("unhardened_clay.tooltip"));
    }
  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    if(!this.hardened) {
      drops.add(new ItemStack(super.getItemDropped(state, world instanceof World ? ((World)world).rand : RANDOM, fortune)));
      return;
    }

    drops.add(GradientItems.CLAY_BUCKET.getItemStack());
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
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
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
}
