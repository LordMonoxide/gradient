package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.network.PacketUpdateBronzeBoilerSteamSink;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockBronzeBoiler extends HeatSinkerBlock {
  public static final IUnlistedProperty<Integer> WATER_LEVEL = null; //TODO new Properties.PropertyAdapter<>(PropertyInteger.create("waterLevel", 0, TileBronzeBoiler.WATER_CAPACITY));
  public static final IUnlistedProperty<Integer> STEAM_LEVEL = null; //TODO new Properties.PropertyAdapter<>(PropertyInteger.create("SteamLevel", 0, TileBronzeBoiler.STEAM_CAPACITY));
  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  private static final Fluid WATER = null; //TODO FluidRegistry.getFluid("water");

  public BlockBronzeBoiler() {
    super("bronze_boiler", Properties.create(GradientBlocks.MATERIAL_BRONZE_MACHINE).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  public TileBronzeBoiler createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileBronzeBoiler();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
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

        te.useBucket(player, hand, world, pos, side);

        return true;
      }

      NetworkHooks.openGui((EntityPlayerMP)player, te, pos);
    }

    return true;
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor) {
    super.neighborChanged(state, world, pos, block, neighbor);

    final TileEntity te = world.getTileEntity(pos);

    if(!(te instanceof TileBronzeBoiler)) {
      return;
    }

    final BlockPos rel = neighbor.subtract(pos);
    final EnumFacing side = EnumFacing.getFacingFromVector(rel.getX(), rel.getY(), rel.getZ());

    if(side == EnumFacing.UP) {
      ((TileBronzeBoiler)te).updateOutput(neighbor);
      PacketUpdateBronzeBoilerSteamSink.send(pos, neighbor);
    }
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
    builder.add(FACING/*, WATER_LEVEL, STEAM_LEVEL*/); //TODO
  }

  //TODO
/*
  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState) state;

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileBronzeBoiler) {
      return extendedState
        .with(WATER_LEVEL, ((TileBronzeBoiler)te).getWaterLevel())
        .with(STEAM_LEVEL, ((TileBronzeBoiler)te).getSteamLevel());
    }

    return extendedState.withProperty(WATER_LEVEL, 0).with(STEAM_LEVEL, 0);
  }
*/

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
  }
}
