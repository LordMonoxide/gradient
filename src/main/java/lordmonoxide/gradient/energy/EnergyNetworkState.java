package lordmonoxide.gradient.energy;

import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public class EnergyNetworkState {
  private final Long2FloatMap storages = new Long2FloatRBTreeMap();
  private Capability<? extends IEnergyStorage> storageCap;
  private Capability<? extends IEnergyTransfer> transferCap;

  public boolean isDirty() {
    return !this.storages.isEmpty();
  }

  void reset() {
    this.storages.clear();
  }

  void setCapabilities(final Capability<? extends IEnergyStorage> storage, final Capability<? extends IEnergyTransfer> transfer) {
    this.storageCap = storage;
    this.transferCap = transfer;
  }

  void add(final BlockPos pos, final EnumFacing facing, final float energy) {
    this.storages.put(WorldUtils.serializeBlockPosAndFacing(pos, facing), energy);
  }

  void add(final long serialized, final float energy) {
    this.storages.put(serialized, energy);
  }

  public int size() {
    return this.storages.size();
  }

  public ObjectSet<Long2FloatMap.Entry> entries() {
    return this.storages.long2FloatEntrySet();
  }

  public Capability<? extends IEnergyStorage> getStorageCapability() {
    return this.storageCap;
  }

  public Capability<? extends IEnergyTransfer> getTransferCapability() {
    return this.transferCap;
  }
}
