package lordmonoxide.gradient.blocks.kinetic.handcrank;

import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileHandCrank extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  private final IKineticEnergyStorage storage = new KineticEnergyStorage(5.0f, 0.0f, 5.0f);

  private int crankTicks;
  private boolean cranking;

  @Override
  public void onLoad() {

  }

  public void crank() {
    this.cranking = true;
  }

  @Override
  public void update() {
    if(this.cranking) {
      this.crankTicks++;

      if(this.crankTicks >= 20) {
        this.cranking = false;
        this.storage.addEnergy(5.0f);
        this.markDirty();
      }
    }
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      return facing == this.world.getBlockState(this.pos).getValue(BlockHandCrank.FACING).getOpposite();
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      if(facing == this.world.getBlockState(this.pos).getValue(BlockHandCrank.FACING).getOpposite()) {
        return STORAGE.cast(this.storage);
      }
    }

    return super.getCapability(capability, facing);
  }
}
