package lordmonoxide.gradient.blocks.heat;

import lordmonoxide.gradient.network.PacketUpdateHeatNeighbours;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class HeatSinkerBlock extends Block {
  protected HeatSinkerBlock(final Block.Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public abstract TileEntity createTileEntity(IBlockState state, IBlockReader world);

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor) {
    super.neighborChanged(state, world, pos, block, neighbor);

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof HeatSinker) {
      ((HeatSinker)te).updateSink(neighbor);
      PacketUpdateHeatNeighbours.send(pos, neighbor);
    }
  }
}
