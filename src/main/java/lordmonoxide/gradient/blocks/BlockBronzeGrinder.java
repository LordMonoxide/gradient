package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class BlockBronzeGrinder extends GradientBlock {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockBronzeGrinder() {
    super("bronze_grinder", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_BRONZE_MACHINE); //$NON-NLS-1$
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public TileBronzeGrinder createTileEntity(final World world, final IBlockState state) {
    return new TileBronzeGrinder();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileBronzeGrinder te = (TileBronzeGrinder)world.getTileEntity(pos);

        if(te == null) {
          return false;
        }

        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          return te.useBucket(player, hand, world, pos, side);
        }

        player.openGui(GradientMod.instance, GradientGuiHandler.BRONZE_GRINDER, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }

    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
    return new BlockStateContainer.Builder(this).add(FACING).build();
  }
}
