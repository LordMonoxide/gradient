package lordmonoxide.gradient.energy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class World implements IBlockReader {
  private final Map<BlockPos, TileEntity> tes = new HashMap<>();

  public TileEntity addTileEntity(final BlockPos pos, final TileEntity te) {
    te.setPos(pos);
    this.tes.put(pos, te);
    return te;
  }

  public void removeTileEntity(final BlockPos pos) {
    this.tes.remove(pos);
  }

  @Nullable
  @Override
  public TileEntity getTileEntity(final BlockPos pos) {
    return this.tes.get(pos);
  }

  @Override
  public IBlockState getBlockState(final BlockPos pos) {
    return null;
  }

  @Override
  public IFluidState getFluidState(final BlockPos blockPos) {
    return null;
  }
}
