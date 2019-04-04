package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileManualGrinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class BlockManualGrinder extends GradientBlock {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 15.0d / 16.0d, 2.0d / 16.0d, 15.0d / 16.0d);

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public BlockManualGrinder() {
    super("manual_grinder", Properties.create(Material.ROCK).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  public TileManualGrinder createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileManualGrinder();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
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
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean isMoving) {
    final TileEntity tile = world.getTileEntity(pos);

    if(!(tile instanceof TileManualGrinder)) {
      return;
    }

    final TileManualGrinder grinder = (TileManualGrinder)tile;
    final ItemStackHandler inv = (ItemStackHandler)grinder.getCapability(ITEM_HANDLER_CAPABILITY, null);

    for(int i = 0; i < inv.getSlots(); i++) {
      if(!inv.getStackInSlot(i).isEmpty()) {
        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i)));
      }
    }

    super.onReplaced(state, world, pos, newState, isMoving);
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
