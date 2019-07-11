package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.containers.BronzeBoilerContainer;
import lordmonoxide.gradient.network.PacketUpdateBronzeBoilerSteamSink;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockBronzeBoiler extends HeatSinkerBlock implements INamedContainerProvider {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  private static final Fluid WATER = null; //TODO FluidRegistry.getFluid("water");

  public BlockBronzeBoiler() {
    super(Properties.create(GradientMaterials.MATERIAL_BRONZE_MACHINE).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public TileBronzeBoiler createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileBronzeBoiler();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!player.isSneaking()) {
      final TileBronzeBoiler te = (TileBronzeBoiler)world.getTileEntity(pos);

      if(te == null) {
        return false;
      }

      if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
        //TODO: nulls
        final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand)).orElse(null);

        // Make sure the fluid handler is either empty, or contains water
        if(fluid != null && fluid.getFluid() != WATER) {
          return true;
        }

        te.useBucket(player, hand, world, pos, hit.getFace());

        return true;
      }

      NetworkHooks.openGui((ServerPlayerEntity)player, this, pos);
    }

    return true;
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
    world.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final BlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor, final boolean isMoving) {
    super.neighborChanged(state, world, pos, block, neighbor, isMoving);

    final TileEntity te = world.getTileEntity(pos);

    if(!(te instanceof TileBronzeBoiler)) {
      return;
    }

    final BlockPos rel = neighbor.subtract(pos);
    final Direction side = Direction.getFacingFromVector(rel.getX(), rel.getY(), rel.getZ());

    if(side == Direction.UP) {
      ((TileBronzeBoiler)te).updateOutput(neighbor);
      PacketUpdateBronzeBoilerSteamSink.send(pos, neighbor);
    }
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
  public boolean isSolid(final BlockState state) {
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final BlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.getNameTextComponent();
  }

  @Nullable
  @Override
  public Container createMenu(final int id, final PlayerInventory playerInv, final PlayerEntity player) {
    final TileBronzeBoiler boiler = WorldUtils.getTileEntity(player.world, player.getPosition(), TileBronzeBoiler.class);

    if(boiler != null) {
      return new BronzeBoilerContainer(id, playerInv, boiler);
    }

    return null;
  }
}
