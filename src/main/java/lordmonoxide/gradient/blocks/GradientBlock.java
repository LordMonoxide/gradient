package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class GradientBlock extends Block {
  public GradientBlock(final String name, final CreativeTabs creativeTab, final Material material, final MapColor mapColor) {
    super(material, mapColor);
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setCreativeTab(creativeTab);
  }

  public GradientBlock(final String name, final CreativeTabs creativeTab, final Material material) {
    this(name, material);
    this.setCreativeTab(creativeTab);
  }

  public GradientBlock(final String name, final Material material) {
    super(material, material.getMaterialMapColor());
    this.setRegistryName(name);
    this.setTranslationKey(name);
  }
}
