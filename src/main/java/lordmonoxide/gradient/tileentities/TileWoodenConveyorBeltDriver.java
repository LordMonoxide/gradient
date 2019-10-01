package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileWoodenConveyorBeltDriver extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage node = new KineticEnergyStorage(1.0f, 1.0f, 0.0f);

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).connect(this.pos, this);
  }

  public void remove() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).disconnect(this.pos);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      //TODO
      return true;
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      //TODO
      return STORAGE.cast(this.node);
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    nbt.setTag("Energy", this.node.serializeNbt());
    return super.writeToNBT(nbt);
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    final NBTTagCompound energy = nbt.getCompoundTag("Energy");
    this.node.deserializeNbt(energy);
    super.readFromNBT(nbt);
  }
}
