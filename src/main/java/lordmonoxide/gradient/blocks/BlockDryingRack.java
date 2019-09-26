package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileDryingRack;
import lordmonoxide.gradient.utils.WorldUtils;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BlockDryingRack extends GradientBlock {
  private static final AxisAlignedBB AABB_NORTH  = new AxisAlignedBB( 0.0d,         13.0d / 16.0d,  0.0d,         1.0d,         1.0d, 2.0d / 16.0d);
  private static final AxisAlignedBB AABB_SOUTH  = new AxisAlignedBB( 0.0d,         13.0d / 16.0d, 14.0d / 16.0d, 1.0d,         1.0d, 1.0d);
  private static final AxisAlignedBB AABB_EAST   = new AxisAlignedBB(14.0d / 16.0d, 13.0d / 16.0d,  0.0d,         1.0d,         1.0d, 1.0d);
  private static final AxisAlignedBB AABB_WEST   = new AxisAlignedBB( 0.0d,         13.0d / 16.0d,  0.0d,         2.0d / 16.0d, 1.0d, 1.0d);
  private static final AxisAlignedBB AABB_DOWN_Z = new AxisAlignedBB( 0.0d,         13.0d / 16.0d,  7.0d / 16.0d, 1.0d,         1.0d, 9.0d / 16.0d);
  private static final AxisAlignedBB AABB_DOWN_X = new AxisAlignedBB( 7.0d / 16.0d, 13.0d / 16.0d,  0.0d        , 9.0d / 16.0d, 1.0d, 1.0d);

  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  public static final PropertyBool ROOF = PropertyBool.create("roof");

  public BlockDryingRack() {
    super("drying_rack", CreativeTabs.TOOLS, Material.WOOD);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ROOF, false));
    this.setHardness(1.0f);
  }

  @Override
  public TileDryingRack createTileEntity(final World world, final IBlockState state) {
    return new TileDryingRack();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      return true;
    }

    if(!player.isSneaking()) {
      final TileDryingRack te = WorldUtils.getTileEntity(world, pos, TileDryingRack.class);

      if(te == null) {
        return false;
      }

      if(te.hasItem()) {
        ItemHandlerHelper.giveItemToPlayer(player, te.takeItem());
        return true;
      }

      final ItemStack held = player.getHeldItem(hand);

      if(!held.isEmpty()) {
        final ItemStack remaining = te.insertItem(held.copy(), player);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }
    }

    return true;
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    final TileDryingRack te = WorldUtils.getTileEntity(world, pos, TileDryingRack.class);

    if(te != null && te.hasItem()) {
      world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), te.getItem()));
    }

    super.breakBlock(world, pos, state);
  }

  @Override
  public boolean canPlaceBlockOnSide(final World world, final BlockPos pos, final EnumFacing side) {
    return this.canPlaceBlockAt(world, pos) && side != EnumFacing.UP;
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    if(facing != EnumFacing.DOWN) {
      return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing.getOpposite()).withProperty(ROOF, false);
    }

    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing()).withProperty(ROOF, true);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0b111);
    final boolean roof = (meta & 0b1000) != 0;
    return this.getDefaultState().withProperty(FACING, facing).withProperty(ROOF, roof);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex() | (state.getValue(ROOF) ? 0b1000 : 0);
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
    return new BlockStateContainer.Builder(this).add(FACING, ROOF).build();
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

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    final EnumFacing facing = state.getValue(FACING);

    if(!state.getValue(ROOF)) {
      switch(facing) {
        case NORTH:
          return AABB_NORTH;

        case SOUTH:
          return AABB_SOUTH;

        case EAST:
          return AABB_EAST;

        case WEST:
          return AABB_WEST;
      }
    } else {
      switch(facing) {
        case NORTH:
        case SOUTH:
          return AABB_DOWN_Z;

        case EAST:
        case WEST:
          return AABB_DOWN_X;
      }
    }

    return AABB_DOWN_Z;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    return null;
  }
}
