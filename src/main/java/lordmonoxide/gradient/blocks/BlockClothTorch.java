package lordmonoxide.gradient.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockClothTorch extends BlockTorch {
  public BlockClothTorch() {
    super();
    this.setRegistryName("cloth_torch");
    this.setTranslationKey("cloth_torch");
    this.setHardness(0.0F);
    this.setLightLevel(0.67F);
    this.setSoundType(SoundType.WOOD);
  }
}
