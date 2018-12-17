package lordmonoxide.gradient.blocks.kinetic.handcrank;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHandCrank extends GradientBlock {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockHandCrank() {
    super("hand_crank", CreativeTabs.TOOLS, Material.CIRCUITS);
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      return true;
    }

    final TileEntity te = world.getTileEntity(pos);

    if(!(te instanceof TileHandCrank)) {
      return true;
    }

    ((TileHandCrank)te).crank();
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
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
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
    return new BlockStateContainer(this, FACING);
  }
}
