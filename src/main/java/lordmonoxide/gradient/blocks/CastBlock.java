package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class CastBlock extends GradientBlock {
  public CastBlock(final GradientMetals.Metal metal) {
    super("cast_block." + metal.name, CreativeTabs.MATERIALS, Material.IRON);
    this.setHarvestLevel("pickaxe", 2);
    this.setHardness(5.0f);
    this.setResistance(10.0f);
  }
}
