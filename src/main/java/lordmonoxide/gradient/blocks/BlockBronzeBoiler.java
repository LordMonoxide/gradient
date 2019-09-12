package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.network.PacketUpdateBronzeBoilerSteamSink;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBronzeBoiler extends HeatSinkerBlock {
  public static final IUnlistedProperty<Integer> WATER_LEVEL = new Properties.PropertyAdapter<>(PropertyInteger.create("waterLevel", 0, TileBronzeBoiler.WATER_CAPACITY));
  public static final IUnlistedProperty<Integer> STEAM_LEVEL = new Properties.PropertyAdapter<>(PropertyInteger.create("SteamLevel", 0, TileBronzeBoiler.STEAM_CAPACITY));
  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  private static final Fluid WATER = FluidRegistry.getFluid("water");

  public BlockBronzeBoiler() {
    super("bronze_boiler", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_BRONZE_MACHINE); //$NON-NLS-1$
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public TileBronzeBoiler createTileEntity(final World world, final IBlockState state) {
    return new TileBronzeBoiler();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!player.isSneaking()) {
      final TileBronzeBoiler te = (TileBronzeBoiler)world.getTileEntity(pos);

      if(te == null) {
        return false;
      }

      if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
        final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));

        // Make sure the fluid handler is either empty, or contains water
        if(fluid != null && fluid.getFluid() != WATER) {
          return true;
        }

        te.useBucket(player, hand, world, pos, side);

        return true;
      }

      player.openGui(GradientMod.instance, GradientGuiHandler.BRONZE_BOILER, world, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @SuppressWarnings("deprecation")
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
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
    return this.getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    final IUnlistedProperty[] unlisted = {WATER_LEVEL, STEAM_LEVEL};
    final IProperty[] listed = {FACING};

    return new ExtendedBlockState(this, listed, unlisted);
  }

  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState) state;

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileBronzeBoiler) {
      return extendedState
        .withProperty(WATER_LEVEL, ((TileBronzeBoiler)te).getWaterLevel())
        .withProperty(STEAM_LEVEL, ((TileBronzeBoiler)te).getSteamLevel());
    }

    return extendedState.withProperty(WATER_LEVEL, 0).withProperty(STEAM_LEVEL, 0);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
  }
}
