package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;

public interface Hardenable {
  Block getHardened();
  int getHardeningTime();
}
