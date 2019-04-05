package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileDryingRack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

//TODO: check if drying rack can be placed on the top of blocks

public class BlockDryingRack extends GradientBlock {
  private static final VoxelShape SHAPE_NORTH  = Block.makeCuboidShape( 0.0d, 13.0d,  0.0d, 16.0d, 16.0d,  2.0d);
  private static final VoxelShape SHAPE_SOUTH  = Block.makeCuboidShape( 0.0d, 13.0d, 14.0d, 16.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_EAST   = Block.makeCuboidShape(14.0d, 13.0d,  0.0d, 16.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_WEST   = Block.makeCuboidShape( 0.0d, 13.0d,  0.0d,  2.0d, 16.0d, 16.0d);
  private static final VoxelShape SHAPE_DOWN_Z = Block.makeCuboidShape( 0.0d, 13.0d,  7.0d, 16.0d, 16.0d,  9.0d);
  private static final VoxelShape SHAPE_DOWN_X = Block.makeCuboidShape( 7.0d, 13.0d,  0.0d,  9.0d, 16.0d, 16.0d);

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
  public static final BooleanProperty ROOF = BooleanProperty.create("roof");

  public BlockDryingRack() {
    super("drying_rack", Properties.create(Material.WOOD).hardnessAndResistance(1.0f).doesNotBlockMovement());
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(ROOF, false));
  }

  @Override
  public TileDryingRack createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileDryingRack();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
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
  public void onReplaced(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean isMoving) {
    final TileDryingRack te = (TileDryingRack)world.getTileEntity(pos);

    if(te != null) {
      if(te.hasItem()) {
        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), te.getItem()));
      }
    }

    super.onReplaced(state, world, pos, newState, isMoving);
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    if(context.getFace() != EnumFacing.DOWN) {
      return super.getStateForPlacement(context).with(FACING, context.getFace().getOpposite()).with(ROOF, false);
    }

    return super.getStateForPlacement(context).with(FACING, context.getPlayer().getHorizontalFacing()).with(ROOF, true);
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
    builder.add(FACING, ROOF);
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

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final IBlockState state, final IBlockReader source, final BlockPos pos) {
    final EnumFacing facing = state.get(FACING);

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
