package lordmonoxide.gradient.energy;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityEnergyTransfer {
  private CapabilityEnergyTransfer() { }

  public static void register() {
    CapabilityManager.INSTANCE.register(IEnergyTransfer.class, new Capability.IStorage<IEnergyTransfer>() {
      @Override
      public NBTBase writeNBT(final Capability<IEnergyTransfer> capability, final IEnergyTransfer instance, final EnumFacing side) {
        return null;
      }

      @Override
      public void readNBT(final Capability<IEnergyTransfer> capability, final IEnergyTransfer instance, final EnumFacing side, final NBTBase nbt) {

      }
    },
    EnergyTransfer::new);
  }
}
