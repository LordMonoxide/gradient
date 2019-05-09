package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockBronzeGrinder extends Block {
  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public BlockBronzeGrinder() {
    super(Properties.create(GradientMaterials.MATERIAL_BRONZE_MACHINE).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public TileBronzeGrinder createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileBronzeGrinder();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileBronzeGrinder te = WorldUtils.getTileEntity(world, pos, TileBronzeGrinder.class);

        if(te == null) {
          return false;
        }

        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          return te.useBucket(player, hand, world, pos, side);
        }

        NetworkHooks.openGui((EntityPlayerMP)player, te, pos);
      }
    }

    return true;
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
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
    builder.add(FACING);
  }
}
