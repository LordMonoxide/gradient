package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockBronzeGrinder extends Block {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public BlockBronzeGrinder() {
    super(Properties.create(GradientMaterials.MATERIAL_BRONZE_MACHINE).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public boolean hasTileEntity(final BlockState state) {
    return true;
  }

  @Override
  public TileBronzeGrinder createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileBronzeGrinder();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileBronzeGrinder te = WorldUtils.getTileEntity(world, pos, TileBronzeGrinder.class);

        if(te == null) {
          return false;
        }

        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          return te.useBucket(player, hand, world, pos, hit.getFace());
        }

        NetworkHooks.openGui((ServerPlayerEntity)player, te, pos);
      }
    }

    return true;
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(final BlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(final BlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }
}
