package lordmonoxide.gradient.blocks.torch;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTorchLit extends BlockTorch {
  public BlockTorchLit(final String name, final float light) {
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setHardness(0.0f);
    this.setLightLevel(light);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(false);
  }

  @Override
  public int tickRate(final World world) {
    return 9600;
  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    world.setBlockState(pos, GradientBlocks.FIBRE_TORCH_UNLIT.getDefaultState().withProperty(BlockTorch.FACING, state.getValue(BlockTorch.FACING)));
  }

  @Override
  public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
    super.onBlockAdded(world, pos, state);
    world.scheduleUpdate(pos, this, this.tickRate(world));
  }
}
