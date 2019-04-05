package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCast extends GradientBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);

  public static BlockClayCast hardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, true);
  }

  public static BlockClayCast unhardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, false);
  }

  public final GradientCasts.Cast cast;
  private final boolean hardened;

  private BlockClayCast(final GradientCasts.Cast cast, final boolean hardened) {
    super("clay_cast" + '.' + cast.name + '.' + (hardened ? "hardened" : "unhardened"), Properties.create(hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.cast = cast;
    this.hardened = hardened;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TextComponentTranslation("unhardened_clay.tooltip"));
    } else {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        tooltip.add(new TextComponentTranslation("block.gradient.clay_cast.hardened.tooltip"));
        final String metalName = I18n.format("fluid." + metal.name);
        final int metalAmount = this.cast.amountForMetal(metal);
        tooltip.add(new TextComponentTranslation("block.gradient.clay_cast.hardened.metal_amount", metalName, metalAmount));
      }
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(face == EnumFacing.DOWN) {
      return BlockFaceShape.SOLID;
    }

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
  public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    return SHAPE;
  }
}
