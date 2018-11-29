package lordmonoxide.gradient.energy.kinetic;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityKineticEnergy {
  private CapabilityKineticEnergy() { }

  public static void register() {
    CapabilityManager.INSTANCE.register(IKineticEnergyStorage.class, new Capability.IStorage<IKineticEnergyStorage>() {
      @Override
      public NBTBase writeNBT(final Capability<IKineticEnergyStorage> capability, final IKineticEnergyStorage instance, final EnumFacing side) {
        return new NBTTagFloat(instance.getEnergyStored());
      }

      @Override
      public void readNBT(final Capability<IKineticEnergyStorage> capability, final IKineticEnergyStorage instance, final EnumFacing side, final NBTBase nbt) {
        if(!(instance instanceof KineticEnergyStorage)) {
          throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }

        ((KineticEnergyStorage)instance).energy = ((NBTPrimitive)nbt).getFloat();
      }
    },
    () -> new KineticEnergyStorage(1000.0f));
  }
}
