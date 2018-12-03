package lordmonoxide.gradient.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityEnergyTransfer {
  private CapabilityEnergyTransfer() { }

  public static void register() {
    CapabilityManager.INSTANCE.register(IEnergyTransfer.class, new Capability.IStorage<IEnergyTransfer>() {
      @Override
      public NBTBase writeNBT(final Capability<IEnergyTransfer> capability, final IEnergyTransfer instance, final EnumFacing side) {
        return new NBTTagFloat(0.0f); // TODO
      }

      @Override
      public void readNBT(final Capability<IEnergyTransfer> capability, final IEnergyTransfer instance, final EnumFacing side, final NBTBase nbt) {
        if(!(instance instanceof EnergyTransfer)) {
          throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
        }

        //TODO ((EnergyTransfer)instance).energy = ((NBTPrimitive)nbt).getFloat();
      }
    },
    EnergyTransfer::new);
  }
}
