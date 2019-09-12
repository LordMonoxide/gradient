package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Ore;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockOre extends Block {
  public final Ore.Metal ore;

  public BlockOre(final Ore.Metal ore) {
    super(Material.ROCK, MapColor.GRAY);
    this.setRegistryName(GradientMod.resource("ore." + ore.name));
    this.setTranslationKey("ore." + ore.name);
    this.setCreativeTab(CreativeTabs.MATERIALS);
    this.setHardness(ore.metal.hardness);
    this.setResistance(5.0f);
    this.setHarvestLevel("pickaxe", Math.max(0, ore.metal.harvestLevel - 1));
    this.ore = ore;
  }
}
