package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockHardenedPlanks extends Block {
  public BlockHardenedPlanks() {
    super(Properties.create(Material.WOOD).hardnessAndResistance(2.0f));
  }

  @Nullable
  @Override
  public ToolType getHarvestTool(final IBlockState state) {
    return ToolType.AXE;
  }

  @Override
  public int getHarvestLevel(final IBlockState state) {
    return 0;
  }
}
