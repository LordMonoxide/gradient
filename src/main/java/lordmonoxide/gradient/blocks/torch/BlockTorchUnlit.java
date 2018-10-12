package lordmonoxide.gradient.blocks.torch;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockTorchUnlit extends BlockTorch {
  public BlockTorchUnlit(final String name) {
    super();
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setHardness(0.0f);
    this.setSoundType(SoundType.WOOD);
  }
}
