package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;

public class GradientBlock extends Block {
  public GradientBlock(final String name, final Block.Properties properties) {
    super(properties);
    this.setRegistryName(name);
  }
}
