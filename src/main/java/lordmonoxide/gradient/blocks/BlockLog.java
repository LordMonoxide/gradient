package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockLog extends BlockRotatedPillar {
  public BlockLog() {
    super(Properties.create(Material.WOOD).hardnessAndResistance(2.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, EnumFacing.Axis.Y));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(AXIS);
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
