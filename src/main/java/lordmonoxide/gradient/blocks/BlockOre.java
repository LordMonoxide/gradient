package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.science.geology.Ore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockOre extends Block {
  public final Ore.Metal ore;

  public BlockOre(final Ore.Metal ore) {
    super(Properties.create(Material.ROCK).hardnessAndResistance(ore.metal.hardness, 5.0f));
    this.ore = ore;
  }

  @Nullable
  @Override
  public ToolType getHarvestTool(final IBlockState state) {
    return ToolType.PICKAXE;
  }

  @Override
  public int getHarvestLevel(final IBlockState state) {
    return Math.max(0, this.ore.metal.harvestLevel - 1);
  }
}
