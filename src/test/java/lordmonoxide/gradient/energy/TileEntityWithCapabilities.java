package lordmonoxide.gradient.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TileEntityWithCapabilities extends TileEntity {
  public static TileEntityWithCapabilities sink() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode(1000.0f, 32.0f, 0.0f, 0.0f));
  }

  public static TileEntityWithCapabilities sink(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode(1000.0f, 32.0f, 0.0f, 0.0f), sides);
  }

  public static TileEntityWithCapabilities source() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode(1000.0f, 0.0f, 32.0f, 10000000.0f));
  }

  public static TileEntityWithCapabilities source(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode(1000.0f, 0.0f, 32.0f, 10000000.0f), sides);
  }

  public static TileEntityWithCapabilities storage() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode());
  }

  public static TileEntityWithCapabilities storage(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.STORAGE, new StorageNode(), sides);
  }

  public static TileEntityWithCapabilities transfer() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.TRANSFER, new TransferNode());
  }

  public static TileEntityWithCapabilities transfer(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkSegmentTest.TRANSFER, new TransferNode(), sides);
  }

  private final Map<EnumFacing, Map<Capability, Object>> caps = new EnumMap<>(EnumFacing.class);

  public TileEntityWithCapabilities() {
    super(null);

    for(final EnumFacing side : EnumFacing.values()) {
      this.caps.put(side, new HashMap<>());
    }
  }

  public <T> TileEntityWithCapabilities addCapability(final Capability<T> capability, final T obj, final EnumFacing... sides) {
    for(final EnumFacing side : sides) {
      this.caps.get(side).put(capability, obj);
    }

    return this;
  }

  public <T> TileEntityWithCapabilities addCapability(final Capability<T> capability, final T obj) {
    return this.addCapability(capability, obj, EnumFacing.values());
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    return this.caps.get(facing).containsKey(capability) ? LazyOptional.of(() -> (T)this.caps.get(facing).get(capability)) : super.getCapability(capability, facing);
  }
}
