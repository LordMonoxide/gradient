package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileDryingRack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

//TODO: check if drying rack can be placed on the top of blocks

public class BlockDryingRack extends Block {
  private static final VoxelShape SHAPE_NORTH  = Block.makeCuboidShape( 0.0d, 13.0d,  0.0d, 16.0d, 16.0d,  2.0d);
  private static final VoxelShape SHAPE_SOUTH  = Block.makeCuboidShape( 0.0d, 13.0d, 14.0d, 16.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_EAST   = Block.makeCuboidShape(14.0d, 13.0d,  0.0d, 16.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_WEST   = Block.makeCuboidShape( 0.0d, 13.0d,  0.0d,  2.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_DOWN_Z = Block.makeCuboidShape( 0.0d, 13.0d,  7.0d, 16.0d, 16.0d,  9.0d);
  private static final VoxelShape SHAPE_DOWN_X = Block.makeCuboidShape( 7.0d, 13.0d,  0.0d,  9.0d, 16.0d, 16.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static final BooleanProperty ROOF = BooleanProperty.create("roof");

  public BlockDryingRack() {
    super(Properties.create(Material.WOOD).hardnessAndResistance(1.0f).doesNotBlockMovement());
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ROOF, false));
  }

  @Override
  public TileDryingRack createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileDryingRack();
  }

  @Override
  public boolean hasTileEntity(final BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!player.isSneaking()) {
      final TileDryingRack te = (TileDryingRack)world.getTileEntity(pos);

      if(te == null) {
        return false;
      }

      final ItemStack held = player.getHeldItem(hand);

      if(held.isEmpty()) {
        if(te.hasItem()) {
          ItemHandlerHelper.giveItemToPlayer(player, te.takeItem());
          return true;
        }
      } else {
        final ItemStack remaining = te.insertItem(held.copy(), player);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }
    }

    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
    final TileDryingRack te = (TileDryingRack)world.getTileEntity(pos);

    if(te != null) {
      if(te.hasItem()) {
        world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), te.getItem()));
      }
    }

    super.onReplaced(state, world, pos, newState, isMoving);
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    if(context.getFace() != Direction.DOWN) {
      return super.getStateForPlacement(context).with(FACING, context.getFace().getOpposite()).with(ROOF, false);
    }

    return super.getStateForPlacement(context).with(FACING, context.getPlayer().getHorizontalFacing()).with(ROOF, true);
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
    builder.add(FACING, ROOF);
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos, final ISelectionContext context) {
    final Direction facing = state.get(FACING);

    if(!state.get(ROOF)) {
      switch(facing) {
        case NORTH:
          return SHAPE_NORTH;

        case SOUTH:
          return SHAPE_SOUTH;

        case EAST:
          return SHAPE_EAST;

        case WEST:
          return SHAPE_WEST;
      }
    } else {
      switch(facing) {
        case NORTH:
        case SOUTH:
          return SHAPE_DOWN_Z;

        case EAST:
        case WEST:
          return SHAPE_DOWN_X;
      }
    }

    return SHAPE_DOWN_Z;
  }
}
