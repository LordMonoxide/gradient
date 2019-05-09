package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class CastBlock extends Block {
  public CastBlock() {
    super(Properties.create(Material.IRON).hardnessAndResistance(10.0f, 5.0f));
  }

  @Nullable
  @Override
  public ToolType getHarvestTool(final IBlockState state) {
    return ToolType.PICKAXE;
  }

  @Override
  public int getHarvestLevel(final IBlockState state) {
    return 2;
  }
}
