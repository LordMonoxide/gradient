package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public final class BlockClayCast extends Block {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0d, 0.0d, 0.0d, 16.0d, 2.0d, 16.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public static BlockClayCast hardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, true);
  }

  public static BlockClayCast unhardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, false);
  }

  public final GradientCasts.Cast cast;
  private final boolean hardened;

  private BlockClayCast(final GradientCasts.Cast cast, final boolean hardened) {
    super(Properties.create(hardened ? GradientMaterials.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    this.cast = cast;
    this.hardened = hardened;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TranslationTextComponent("unhardened_clay.tooltip"));
    } else {
      for(final Metal metal : Metals.all()) {
        tooltip.add(new TranslationTextComponent("block.gradient.clay_cast.hardened.tooltip"));
        final String metalName = I18n.format("fluid." + metal.name);
        final int metalAmount = this.cast.amountForMetal(metal);
        tooltip.add(new TranslationTextComponent("block.gradient.clay_cast.hardened.metal_amount", metalName, metalAmount));
      }
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final BlockState state, final BlockPos pos, final Direction face) {
    if(face == Direction.DOWN) {
      return BlockFaceShape.SOLID;
    }

    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final BlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos) {
    return SHAPE;
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
    world.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(final BlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(final BlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }
}
