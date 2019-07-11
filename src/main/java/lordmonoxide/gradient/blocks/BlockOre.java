package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.science.geology.Ore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockOre extends Block {
  public final Ore.Metal ore;

  public BlockOre(final Ore.Metal ore) {
    super(Properties.create(Material.ROCK).hardnessAndResistance(ore.metal.hardness, 5.0f).harvestTool(ToolType.PICKAXE).harvestLevel(Math.max(0, ore.metal.harvestLevel - 1)));
    this.ore = ore;
  }
}
