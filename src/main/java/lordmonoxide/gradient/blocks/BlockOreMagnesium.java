package lordmonoxide.gradient.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOreMagnesium extends GradientBlock {
  public BlockOreMagnesium() {
    super("ore_magnesium", CreativeTabs.MATERIALS, Material.ROCK, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(3.0f);
    this.setResistance(5.0f);
  }
}
