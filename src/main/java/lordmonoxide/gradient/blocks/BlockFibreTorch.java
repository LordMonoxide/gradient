package lordmonoxide.gradient.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;

public class BlockFibreTorch extends BlockTorch {
  public BlockFibreTorch() {
    super();
    this.setRegistryName("fibre_torch");
    this.setTranslationKey("fibre_torch");
    this.setHardness(0.0F);
    this.setLightLevel(0.67F);
    this.setSoundType(SoundType.WOOD);
  }
}
