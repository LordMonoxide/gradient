package lordmonoxide.gradient.blocks.kinetic.handcrank;

import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.HashMap;
import java.util.Map;

public class TileHandCrank extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> ENERGY;

  private final Map<BlockPos, IKineticEnergyStorage> neighbours = new HashMap<>();

  private int crankTicks;
  private boolean cranking;

  public void crank() {
    this.cranking = true;
  }

  @Override
  public void update() {
    if(this.cranking) {
      this.crankTicks++;

      if(this.crankTicks >= 20) {
        this.cranking = false;
        this.outputToNeighbours();
      }
    }
  }

  private void outputToNeighbours() {
    if(this.neighbours.isEmpty()) {
      return;
    }

    final float output = 5.0f / this.neighbours.size();

    for(final IKineticEnergyStorage storage : this.neighbours.values()) {
      storage.receiveEnergy(output, false);
    }
  }

  public void updateNeighbour(final IBlockAccess world, final BlockPos pos, final BlockPos neighbour) {
    final BlockPos offset = pos.subtract(neighbour);
    final EnumFacing facing = EnumFacing.getFacingFromVector(offset.getX(), offset.getY(), offset.getZ()).getOpposite();

    if(facing == EnumFacing.DOWN) {
      return;
    }

    this.neighbours.remove(neighbour);

    final TileEntity te = world.getTileEntity(neighbour);

    if(te != null && te.hasCapability(ENERGY, facing)) {
      this.neighbours.put(neighbour, te.getCapability(ENERGY, facing));
    }
  }
}
