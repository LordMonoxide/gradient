package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileMixingBasin;
import lordmonoxide.gradient.utils.WorldUtils;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockMixingBasin extends GradientBlock {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private static final Fluid WATER = null; //TODO: FluidRegistry.getFluid("water");

  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 15.0d / 16.0d, 2.0d / 16.0d, 15.0d / 16.0d);

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public BlockMixingBasin() {
    super("mixing_basin", Properties.create(Material.ROCK).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  public TileMixingBasin createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileMixingBasin();
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

        FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);

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
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean isMoving) {
    final TileMixingBasin mixer = WorldUtils.getTileEntity(world, pos, TileMixingBasin.class);

    if(mixer != null) {
      mixer.getCapability(ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
        for(int i = 0; i < inv.getSlots(); i++) {
          if(!inv.getStackInSlot(i).isEmpty()) {
            world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i)));
          }
        }
      });
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
