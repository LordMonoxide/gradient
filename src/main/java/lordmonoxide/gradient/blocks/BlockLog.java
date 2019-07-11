package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;

public class BlockLog extends RotatedPillarBlock {
  public BlockLog() {
    super(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0f).harvestTool(ToolType.AXE));
    this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(AXIS);
  }
}
