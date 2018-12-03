package lordmonoxide.gradient.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityEnergyStorage {
  private CapabilityEnergyStorage() { }

  public static void register() {
    CapabilityManager.INSTANCE.register(IEnergyStorage.class, new Capability.IStorage<IEnergyStorage>() {
      @Override
      public NBTBase writeNBT(final Capability<IEnergyStorage> capability, final IEnergyStorage instance, final EnumFacing side) {
        return new NBTTagFloat(instance.getEnergyStored());
      }

      @Override
      public void readNBT(final Capability<IEnergyStorage> capability, final IEnergyStorage instance, final EnumFacing side, final NBTBase nbt) {
        if(!(instance instanceof EnergyStorage)) {
          throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }

        ((EnergyStorage)instance).energy = ((NBTPrimitive)nbt).getFloat();
      }
    },
    () -> new EnergyStorage(1000));
  }
}
