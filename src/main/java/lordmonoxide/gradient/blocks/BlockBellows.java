package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileBellows;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBellows extends Block {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  private static final IUnlistedProperty<IModelState> ANIMATION = Properties.AnimationProperty;

  private static final AxisAlignedBB AABB = new AxisAlignedBB(2.0d / 16.0d, 2.0d / 16.0d, 0.0d, 14.0d / 16.0d, 14.0d / 16.0d, 1.0d);

  public BlockBellows() {
    super(Material.WOOD);
    this.setRegistryName("bellows");
    this.setTranslationKey("bellows");
    this.setCreativeTab(CreativeTabs.TOOLS);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(I18n.format("tile.bellows.tooltip"));
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      final TileBellows bellows = WorldUtils.getTileEntity(world, pos, TileBellows.class);

      if(bellows == null) {
        return false;
      }

      if(!player.isSneaking()) {
        bellows.activate();
        return true;
      }
    }

    return true;
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
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    if(facing.getAxis() == EnumFacing.Axis.Y) {
      return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite());
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0b11);
    return this.getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(FACING).add(ANIMATION).build();
  }

  @Override
  public TileBellows createTileEntity(final World world, final IBlockState state) {
    return new TileBellows();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public EnumBlockRenderType getRenderType(final IBlockState state) {
    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }
}
