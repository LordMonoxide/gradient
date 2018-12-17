package lordmonoxide.gradient.blocks.kinetic.flywheel;

import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileFlywheel extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  private final IKineticEnergyStorage energy = new KineticEnergyStorage(100.0f);

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockFlywheel.FACING);
      return facing == myFacing.rotateY() || facing == myFacing.rotateYCCW();
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final EnumFacing myFacing = this.world.getBlockState(this.pos).getValue(BlockFlywheel.FACING);

      if(facing == myFacing.rotateY() || facing == myFacing.rotateYCCW()) {
        return STORAGE.cast(this.energy);
      }
    }

    return super.getCapability(capability, facing);
  }
}
