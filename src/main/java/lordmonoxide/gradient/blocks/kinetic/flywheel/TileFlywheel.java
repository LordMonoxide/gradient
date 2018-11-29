package lordmonoxide.gradient.blocks.kinetic.flywheel;

import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class TileFlywheel extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> ENERGY;

  @Override
  public void update() {

  }
}
