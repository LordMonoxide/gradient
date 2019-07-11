package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileMixingBasin;
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
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockMixingBasin extends Block {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private static final Fluid WATER = null; //TODO: FluidRegistry.getFluid("water");

  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d, 0.0d, 1.0d, 15.0d, 2.0d, 15.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public BlockMixingBasin() {
    super(Properties.create(Material.ROCK).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public TileMixingBasin createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileMixingBasin();
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

      if(!(tile instanceof TileMixingBasin)) {
        return false;
      }

      final TileMixingBasin basin = (TileMixingBasin)tile;

      // Water
      if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
        final LazyOptional<FluidStack> fluidOpt = FluidUtil.getFluidContained(player.getHeldItem(hand));

        if(!fluidOpt.isPresent()) {
          return true;
        }

        //TODO: null
        final FluidStack fluid = fluidOpt.orElse(null);

        // Make sure the fluid handler is either empty, or contains 1000 mB of water
        if(fluid.getFluid() != WATER || fluid.amount < Fluid.BUCKET_VOLUME) {
          return true;
        }

        FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());

        return true;
      }

      // Remove input
      if(player.isSneaking()) {
        for(int slot = 0; slot < TileMixingBasin.INPUT_SIZE; slot++) {
          if(basin.hasInput(slot)) {
            final ItemStack stack = basin.takeInput(slot, player);
            ItemHandlerHelper.giveItemToPlayer(player, stack);
            return true;
          }
        }

        return true;
      }

      // Take stuff out
      if(basin.hasOutput()) {
        final ItemStack stack = basin.takeOutput();
        ItemHandlerHelper.giveItemToPlayer(player, stack);
        return true;
      }

      final ItemStack held = player.getHeldItem(hand);

      // Put stuff in
      if(!held.isEmpty()) {
        final ItemStack remaining = basin.insertItem(held.copy(), player);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }

      basin.mix();
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
    final TileMixingBasin mixer = WorldUtils.getTileEntity(world, pos, TileMixingBasin.class);

    if(mixer != null) {
      mixer.getCapability(ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
        for(int i = 0; i < inv.getSlots(); i++) {
          if(!inv.getStackInSlot(i).isEmpty()) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i)));
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

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos, final ISelectionContext context) {
    return SHAPE;
  }
}
