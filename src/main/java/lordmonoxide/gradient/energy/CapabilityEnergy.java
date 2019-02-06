package lordmonoxide.gradient.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public final class CapabilityEnergy {
  private CapabilityEnergy() { }

  public static <STORAGE extends IEnergyStorage, TRANSFER extends IEnergyTransfer> void register(final Class<STORAGE> storage, final Class<TRANSFER> transfer, final Callable<STORAGE> defaultStorage, final Callable<TRANSFER> defaultTransfer) {
    CapabilityManager.INSTANCE.register(storage, new Capability.IStorage<STORAGE>() {
      @Override
      public NBTBase writeNBT(final Capability<STORAGE> capability, final STORAGE instance, final EnumFacing side) {
        return new NBTTagFloat(instance.getEnergy());
      }

      @Override
      public void readNBT(final Capability<STORAGE> capability, final STORAGE instance, final EnumFacing side, final NBTBase nbt) {
        instance.setEnergy(((NBTPrimitive)nbt).getFloat());
      }
    },
    defaultStorage);

    CapabilityManager.INSTANCE.register(transfer, new Capability.IStorage<TRANSFER>() {
      @Override
      public NBTBase writeNBT(final Capability<TRANSFER> capability, final TRANSFER instance, final EnumFacing side) {
        return null;
      }

      @Override
      public void readNBT(final Capability<TRANSFER> capability, final TRANSFER instance, final EnumFacing side, final NBTBase nbt) {

      }
    },
    defaultTransfer);
  }
}
