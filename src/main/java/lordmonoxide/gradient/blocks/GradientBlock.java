package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class GradientBlock extends Block {
  public GradientBlock(String name, CreativeTabs creative_tab, Material material, MapColor map_color) {
    super(material, map_color);
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.setCreativeTab(creative_tab);
  }
  
  public GradientBlock(String name, CreativeTabs creative_tab, Material material) {
    super(material, material.getMaterialMapColor());
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.setCreativeTab(creative_tab);
  }
}
