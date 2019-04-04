package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCrucibleUnhardened extends GradientBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 1.0d - 1.0d / 16.0d, 0.75d, 1.0d - 1.0d / 16.0d);

  public BlockClayCrucibleUnhardened() {
    super("clay_crucible.unhardened", Properties.create(Material.CLAY).hardnessAndResistance(1.0f, 2.0f));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TextComponentTranslation("unhardened_clay.tooltip"));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final IBlockState state, final IBlockReader source, final BlockPos pos) {
    return SHAPE;
  }
}
