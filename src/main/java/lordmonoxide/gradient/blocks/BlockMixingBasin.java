package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileMixingBasin;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class BlockMixingBasin extends GradientBlock {
  public static IUnlistedProperty<Boolean> HAS_WATER = Properties.toUnlisted(PropertyBool.create("HAS_WATER"));

  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private static final Fluid WATER = FluidRegistry.getFluid("water");

  private static final AxisAlignedBB AABB = new AxisAlignedBB(2.0d / 16.0d, 0.0d, 2.0d / 16.0d, 14.0d / 16.0d, 8.0d / 16.0d, 14.0d / 16.0d);

  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockMixingBasin() {
    super("mixing_basin", CreativeTabs.TOOLS, Material.ROCK);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public TileMixingBasin createTileEntity(final World world, final IBlockState state) {
    return new TileMixingBasin();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      final TileEntity tile = world.getTileEntity(pos);

      if(!(tile instanceof TileMixingBasin)) {
        return false;
      }

      final TileMixingBasin basin = (TileMixingBasin)tile;

      // Water
      if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
        final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));

        // Make sure the fluid handler is either empty, or contains 1000 mB of water
        if(fluid != null && (fluid.getFluid() != WATER || fluid.amount < Fluid.BUCKET_VOLUME)) {
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

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    final TileEntity tile = world.getTileEntity(pos);

    if(!(tile instanceof TileMixingBasin)) {
      return;
    }

    final TileMixingBasin mixer = (TileMixingBasin)tile;
    final ItemStackHandler inv = (ItemStackHandler)mixer.getCapability(ITEM_HANDLER_CAPABILITY, null);

    for(int i = 0; i < inv.getSlots(); i++) {
      if(!inv.getStackInSlot(i).isEmpty()) {
        world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), inv.getStackInSlot(i)));
      }
    }

    super.breakBlock(world, pos, state);
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
    return new BlockStateContainer.Builder(this).add(FACING).add(HAS_WATER).build();
  }

  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState)state;

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileMixingBasin) {
      return extendedState.withProperty(HAS_WATER, ((TileMixingBasin)te).hasFluid());
    }

    return extendedState;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
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
}
