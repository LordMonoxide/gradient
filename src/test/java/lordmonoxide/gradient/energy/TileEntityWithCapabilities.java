package lordmonoxide.gradient.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TileEntityWithCapabilities extends TileEntity {
  public static TileEntityWithCapabilities sink() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, new StorageNode(1000.0f, 32.0f, 0.0f, 0.0f));
  }

  public static TileEntityWithCapabilities source() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, new StorageNode(1000.0f, 0.0f, 32.0f, 10000000.0f));
  }

  public static TileEntityWithCapabilities storage() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, new StorageNode());
  }

  public static TileEntityWithCapabilities storage(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, new StorageNode(), sides);
  }

  public static TileEntityWithCapabilities transfer() {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.TRANSFER, new TransferNode());
  }

  public static TileEntityWithCapabilities transfer(final EnumFacing... sides) {
    return new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.TRANSFER, new TransferNode(), sides);
  }

  private final Map<EnumFacing, Map<Capability, Object>> caps = new EnumMap<>(EnumFacing.class);

  public TileEntityWithCapabilities() {
    for(final EnumFacing side : EnumFacing.VALUES) {
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
    return this.addCapability(capability, obj, EnumFacing.VALUES);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return this.caps.get(facing).containsKey(capability);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    return this.caps.get(facing).containsKey(capability) ? (T)this.caps.get(facing).get(capability) : super.getCapability(capability, facing);
  }
}
