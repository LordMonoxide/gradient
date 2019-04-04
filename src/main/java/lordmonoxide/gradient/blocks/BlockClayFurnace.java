package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayFurnace extends GradientBlock {
  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public static BlockClayFurnace hardened() {
    return new BlockClayFurnace(true);
  }

  public static BlockClayFurnace unhardened() {
    return new BlockClayFurnace(false);
  }

  private final boolean hardened;

  protected BlockClayFurnace(final boolean hardened) {
    super("clay_furnace" + '.' + (hardened ? "hardened" : "unhardened"), Properties.create(hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
    this.hardened = hardened;
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TextComponentTranslation("unhardened_clay.tooltip"));
    } else {
      tooltip.add(new TextComponentTranslation("tile.clay_furnace.hardened.tooltip"));
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(face != EnumFacing.UP && face != EnumFacing.SOUTH) {
      return BlockFaceShape.SOLID;
    }

    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isTopSolid(final IBlockState state) {
    return false;
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState rotate(final IBlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState mirror(final IBlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(FACING);
  }
}
