package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTorchLit extends TorchBlock {
  public static final BooleanProperty STAND = BooleanProperty.create("stand");

  private final int light;
  private final int lightOnStand;

  public BlockTorchLit(final float light, final float lightOnStand, final Properties properties) {
    super(properties.sound(SoundType.WOOD));
    this.light = (int)(15.0f * light);
    this.lightOnStand = (int)(15.0f * lightOnStand);
    this.setDefaultState(this.stateContainer.getBaseState().with(STAND, false));
  }

  @Override
  public boolean ticksRandomly(final BlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public int getLightValue(final BlockState state) {
    return state.get(STAND) ? this.lightOnStand : this.light;
  }

  @Override
  public int tickRate(final IWorldReader world) {
    return 9600;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(final BlockState state, final World world, final BlockPos pos, final Random random) {
    world.setBlockState(pos, GradientBlocks.FIBRE_TORCH_UNLIT.getDefaultState());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(final BlockState state, final World world, final BlockPos pos, final BlockState oldState, final boolean isMoving) {
    world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    super.onBlockAdded(state, world, pos, oldState, isMoving);
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(STAND);
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    final BlockState state = super.getStateForPlacement(context);

    if(context.getFace() == Direction.UP) {
      if(context.getWorld().getBlockState(context.getPos().down()).getBlock() == GradientBlocks.TORCH_STAND) {
        return state.with(STAND, true);
      }
    }

    return state;
  }
}
