package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTorchLit extends BlockTorch {
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
  public boolean ticksRandomly(final IBlockState p_149653_1_) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public int getLightValue(final IBlockState state) {
    return state.get(STAND) ? this.lightOnStand : this.light;
  }

  @Override
  public int tickRate(final IWorldReaderBase world) {
    return 9600;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(final IBlockState state, final World world, final BlockPos pos, final Random random) {
    world.setBlockState(pos, GradientBlocks.FIBRE_TORCH_UNLIT.getDefaultState());
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(final IBlockState state, final World world, final BlockPos pos, final IBlockState oldState) {
    world.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(world));
    super.onBlockAdded(state, world, pos, oldState);
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(STAND);
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    final IBlockState state = super.getStateForPlacement(context);

    if(context.getFace() == EnumFacing.UP) {
      if(context.getWorld().getBlockState(context.getPos().down()).getBlock() == GradientBlocks.TORCH_STAND) {
        return state.with(STAND, true);
      }
    }

    return state;
  }
}
