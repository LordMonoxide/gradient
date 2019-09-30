package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileHandCrank;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHandCrank extends GradientBlock {
  public static final PropertyDirection FACING = PropertyDirection.create("facing");

  public BlockHandCrank() {
    super("hand_crank", CreativeTabs.TOOLS, Material.CIRCUITS);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    super.breakBlock(world, pos, state);

    if(world.isRemote) {
      return;
    }

    WorldUtils.callTileEntity(world, pos, TileHandCrank.class, TileHandCrank::remove);
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      return true;
    }

    final TileHandCrank crank = WorldUtils.getTileEntity(world, pos, TileHandCrank.class);

    if(crank == null) {
      return true;
    }

    boolean leashed = false;
    for(final AbstractHorse horse : world.getEntitiesWithinAABB(AbstractHorse.class, new AxisAlignedBB(pos.getX() - 15.0d, pos.getY() - 15.0d, pos.getZ() - 15.0d, pos.getX() + 15.0d, pos.getY() + 15.0d, pos.getZ() + 15.0d))) {
      if(horse.getLeashed() && horse.getLeashHolder() == player) {
        crank.attachWorker(horse);
        leashed = true;
      }
    }

    if(leashed) {
      return true;
    }

    if(crank.hasWorker()) {
      crank.detachWorkers(player);
      return true;
    }

    crank.crank();
    return true;
  }

  @Override
  public TileHandCrank createTileEntity(final World world, final IBlockState state) {
    return new TileHandCrank();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getIndex();
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
    return new BlockStateContainer.Builder(this).add(FACING).build();
  }
}
