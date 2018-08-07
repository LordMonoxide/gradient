package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOre extends GradientBlock {
  public BlockOre(final GradientMetals.Metal metal) {
    super("ore_" + metal.name, CreativeTabs.MATERIALS, Material.ROCK, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(metal.hardness);
    this.setResistance(5.0f);
  }
}
