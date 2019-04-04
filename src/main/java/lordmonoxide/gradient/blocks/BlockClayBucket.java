package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayBucket extends GradientBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(3.0d / 16.0d, 0.0d, 3.0d / 16.0d, 1.0d - 3.0d / 16.0d, 0.5d, 1.0d - 3.0d / 16.0d);

  public static BlockClayBucket hardened() {
    return new BlockClayBucket(true);
  }

  public static BlockClayBucket unhardened() {
    return new BlockClayBucket(false);
  }

  private final boolean hardened;

  protected BlockClayBucket(final boolean hardened) {
    super("clay_bucket" + '.' + (hardened ? "hardened" : "unhardened"), Properties.create(hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.hardened = hardened;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TextComponentTranslation("unhardened_clay.tooltip"));
    }
  }

  @Override
  public void getDrops(final IBlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
    if(!this.hardened) {
      drops.add(new ItemStack(super.getItemDropped(state, world, pos, fortune)));
      return;
    }

    drops.add(GradientItems.CLAY_BUCKET.getItemStack());
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    return SHAPE;
  }
}
