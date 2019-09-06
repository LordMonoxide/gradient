package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.tileentities.TileClayMetalMixer;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BlockClayMetalMixerHardened extends HeatSinkerBlock {
  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);

  private static final IUnlistedProperty<IModelState> ANIMATION = Properties.AnimationProperty;
  private static final Map<EnumFacing, IProperty<Boolean>> CONNECTED = new EnumMap<>(EnumFacing.class);

  static {
    CONNECTED.put(EnumFacing.NORTH, PropertyBool.create("connected_north"));
    CONNECTED.put(EnumFacing.SOUTH, PropertyBool.create("connected_south"));
    CONNECTED.put(EnumFacing.WEST, PropertyBool.create("connected_west"));
    CONNECTED.put(EnumFacing.EAST, PropertyBool.create("connected_east"));
  }

  protected BlockClayMetalMixerHardened() {
    super("clay_metal_mixer_hardened", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
    this.setDefaultState(this.getDefaultState().withProperty(CONNECTED.get(EnumFacing.NORTH), false).withProperty(CONNECTED.get(EnumFacing.SOUTH), false).withProperty(CONNECTED.get(EnumFacing.WEST), false).withProperty(CONNECTED.get(EnumFacing.EAST), false));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(ANIMATION).add(CONNECTED.get(EnumFacing.NORTH), CONNECTED.get(EnumFacing.SOUTH), CONNECTED.get(EnumFacing.WEST), CONNECTED.get(EnumFacing.EAST)).build();
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState()
      .withProperty(CONNECTED.get(EnumFacing.NORTH), (meta >>> 0 & 0b1) == 1)
      .withProperty(CONNECTED.get(EnumFacing.SOUTH), (meta >>> 1 & 0b1) == 1)
      .withProperty(CONNECTED.get(EnumFacing.WEST),  (meta >>> 2 & 0b1) == 1)
      .withProperty(CONNECTED.get(EnumFacing.EAST),  (meta >>> 3 & 0b1) == 1);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return
      (state.getValue(CONNECTED.get(EnumFacing.NORTH)) ? 1 : 0) << 0 |
      (state.getValue(CONNECTED.get(EnumFacing.SOUTH)) ? 1 : 0) << 1 |
      (state.getValue(CONNECTED.get(EnumFacing.WEST))  ? 1 : 0) << 2 |
      (state.getValue(CONNECTED.get(EnumFacing.EAST))  ? 1 : 0) << 3;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public EnumBlockRenderType getRenderType(final IBlockState state) {
    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(I18n.format("tile.clay_metal_mixer.hardened.tooltip"));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
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

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }

  @Override
  public TileEntity createTileEntity(final World world, final IBlockState state) {
    return new TileClayMetalMixer();
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
      .withProperty(CONNECTED.get(EnumFacing.NORTH), this.getFluidHandler(world, pos.offset(EnumFacing.NORTH)) != null)
      .withProperty(CONNECTED.get(EnumFacing.SOUTH), this.getFluidHandler(world, pos.offset(EnumFacing.SOUTH)) != null)
      .withProperty(CONNECTED.get(EnumFacing.WEST),  this.getFluidHandler(world, pos.offset(EnumFacing.WEST)) != null)
      .withProperty(CONNECTED.get(EnumFacing.EAST),  this.getFluidHandler(world, pos.offset(EnumFacing.EAST)) != null);
  }

  @Override
  public void onNeighborChange(final IBlockAccess world, final BlockPos pos, final BlockPos neighbor) {
    super.onNeighborChange(world, pos, neighbor);

    if(world instanceof World && ((World)world).isRemote) {
      return;
    }

    final TileClayMetalMixer mixer = WorldUtils.getTileEntity(world, pos, TileClayMetalMixer.class);

    if(mixer == null) {
      return;
    }

    final EnumFacing side = WorldUtils.getBlockFacing(pos, neighbor);

    if(side.getAxis().isHorizontal()) {
      mixer.inputUpdated(side);
    } else if(side == EnumFacing.DOWN) {
      mixer.outputUpdated();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor) {
    super.neighborChanged(state, world, pos, block, neighbor);

    final TileClayMetalMixer mixer = WorldUtils.getTileEntity(world, pos, TileClayMetalMixer.class);

    if(mixer == null) {
      return;
    }

    // Output
    if(neighbor.equals(pos.down())) {
      mixer.outputChanged(this.getFluidHandler(world, neighbor));
      return;
    }

    // Inputs
    final EnumFacing side = WorldUtils.getBlockFacing(pos, neighbor);

    if(side == EnumFacing.UP) {
      return;
    }

    mixer.inputChanged(side, this.getFluidHandler(world, neighbor));

    if(mixer.isConnected(side) != state.getValue(CONNECTED.get(side))) {
      world.setBlockState(pos, state.withProperty(CONNECTED.get(side), mixer.isConnected(side)));
      mixer.validate();
      world.setTileEntity(pos, mixer);
    }
  }

  @Nullable
  private IFluidHandler getFluidHandler(final IBlockAccess world, final BlockPos pos) {
    final TileEntity te = world.getTileEntity(pos);

    if(te == null) {
      return null;
    }

    return te.getCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
  }
}
