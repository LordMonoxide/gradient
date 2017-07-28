package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class GradientBlock extends Block {
  public GradientBlock(String name, CreativeTabs creativeTab, Material material, MapColor mapColor) {
    super(material, mapColor);
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.setCreativeTab(creativeTab);
  }
  
  public GradientBlock(String name, CreativeTabs creativeTab, Material material) {
    super(material, material.getMaterialMapColor());
    this.setRegistryName(name);
    this.setUnlocalizedName(name);
    this.setCreativeTab(creativeTab);
  }
}
