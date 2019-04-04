package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.material.Material;

public class BlockOre extends GradientBlock {
  public BlockOre(final GradientMetals.Metal metal) {
    super("ore." + metal.name, Properties.create(Material.ROCK).hardnessAndResistance(metal.hardness, 5.0f));
  }
}
