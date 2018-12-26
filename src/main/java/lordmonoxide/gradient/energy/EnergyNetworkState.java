package lordmonoxide.gradient.energy;

import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EnergyNetworkState {
  private final Long2FloatMap storage = new Long2FloatRBTreeMap();

  public boolean isDirty() {
    return !this.storage.isEmpty();
  }

  void reset() {
    this.storage.clear();
  }

  void add(final BlockPos pos, final EnumFacing facing, final float energy) {
    this.storage.put(BlockPosUtils.serializeBlockPosAndFacing(pos, facing), energy);
  }

  public int size() {
    return this.storage.size();
  }

  public ObjectSet<Long2FloatMap.Entry> entries() {
    return this.storage.long2FloatEntrySet();
  }
}
