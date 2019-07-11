package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileManualGrinder;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockGrindstone extends Block {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d, 0.0d, 1.0d, 15.0d, 2.0d, 15.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public BlockGrindstone() {
    super(Properties.create(Material.ROCK).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public TileManualGrinder createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileManualGrinder();
  }

  @Override
  public boolean hasTileEntity(final BlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      final TileEntity tile = world.getTileEntity(pos);

      if(!(tile instanceof TileManualGrinder)) {
        return false;
      }

      final TileManualGrinder grinder = (TileManualGrinder)tile;

      // Remove input
      if(player.isSneaking()) {
        if(grinder.hasInput()) {
          final ItemStack stack = grinder.takeInput();
          ItemHandlerHelper.giveItemToPlayer(player, stack);
        }

        return true;
      }

      // Take stuff out
      if(grinder.hasOutput()) {
        final ItemStack stack = grinder.takeOutput();
        ItemHandlerHelper.giveItemToPlayer(player, stack);

        return true;
      }

      final ItemStack held = player.getHeldItem(hand);

      // Put stuff in
      if(!held.isEmpty()) {
        final ItemStack remaining = grinder.insertItem(held.copy(), player);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }

      grinder.crank();
      return true;
    }

    return true;
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
    final TileManualGrinder grinder = WorldUtils.getTileEntity(world, pos, TileManualGrinder.class);

    if(grinder != null) {
      grinder.getCapability(ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
        for(int i = 0; i < inv.getSlots(); i++) {
          if(!inv.getStackInSlot(i).isEmpty()) {
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i)));
          }
        }
      });
    }

    super.onReplaced(state, world, pos, newState, isMoving);
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

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final BlockState state, final BlockPos pos, final Direction face) {
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
  @Deprecated
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos) {
    return SHAPE;
  }
}
