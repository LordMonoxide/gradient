package lordmonoxide.gradient.blocks.torch;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTorchLit extends BlockTorch {
  public BlockTorchLit(final String name, final float light) {
    super();
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setHardness(0.0f);
    this.setLightLevel(light);
    this.setSoundType(SoundType.WOOD);
  }

  @Override
  public int tickRate(final World world) {
    return 40;
  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {

  }
}
