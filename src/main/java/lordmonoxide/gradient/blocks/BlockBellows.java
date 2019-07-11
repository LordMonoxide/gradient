package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileBellows;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBellows extends Block {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  //TODO private static final IUnlistedProperty<IModelState> ANIMATION = Properties.AnimationProperty;

  private static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(2.0d, 2.0d, 0.0d, 14.0d, 14.0d, 13.0d);
  private static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(2.0d, 2.0d, 3.0d, 14.0d, 14.0d,  1.0d);
  private static final VoxelShape SHAPE_EAST  = Block.makeCuboidShape(3.0d, 2.0d, 2.0d,  1.0d, 14.0d, 14.0d);
  private static final VoxelShape SHAPE_WEST  = Block.makeCuboidShape(0.0d, 2.0d, 2.0d, 13.0d, 14.0d, 14.0d);

  public BlockBellows() {
    super(Properties.create(Material.CIRCUITS).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TranslationTextComponent("block.gradient.bellows.tooltip"));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      final TileBellows bellows = WorldUtils.getTileEntity(world, pos, TileBellows.class);

      if(bellows == null) {
        return false;
      }

      if(!player.isSneaking()) {
        bellows.activate();
        return true;
      }
    }

    return true;
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
  public boolean isSolid(final BlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final BlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos) {
    switch(state.get(FACING)) {
      case NORTH:
        return SHAPE_NORTH;
      case SOUTH:
        return SHAPE_SOUTH;
      case EAST:
        return SHAPE_EAST;
      case WEST:
        return SHAPE_WEST;
    }

    return SHAPE_NORTH;
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    if(context.getFace().getAxis() == Direction.Axis.Y) {
      return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    return super.getStateForPlacement(context).with(FACING, context.getFace().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public BlockState rotate(final BlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public BlockState mirror(final BlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING); //TODO .add(ANIMATION);
  }

  @Override
  public TileBellows createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileBellows();
  }

  @Override
  public boolean hasTileEntity(final BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public BlockRenderType getRenderType(final BlockState state) {
    return BlockRenderType.ENTITYBLOCK_ANIMATED;
  }
}
