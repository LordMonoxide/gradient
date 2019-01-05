package lordmonoxide.gradient.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockHardenedPlanks extends GradientBlock {
  public BlockHardenedPlanks() {
    super("hardened_planks", CreativeTabs.BUILDING_BLOCKS, Material.WOOD);
    this.setHarvestLevel("axe", 0);
    this.setHardness(2.0f);
  }
}
