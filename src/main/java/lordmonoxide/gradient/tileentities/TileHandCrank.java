package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockHandCrank;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.LinkedList;

public class TileHandCrank extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private static final int MAX_WORKERS = 4;
  private static final double WORKER_DISTANCE = 3.0d;

  private final IKineticEnergyStorage storage = new KineticEnergyStorage(5.0f, 0.0f, 5.0f);

  private final LinkedList<WorkerData> workers = new LinkedList<>();

  private int crankTicks;
  private boolean cranking;

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).connect(this.pos, this);
  }

  public void remove() {
    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).disconnect(this.pos);
  }

  public void crank() {
    this.cranking = true;
  }

  public boolean hasWorker() {
    return !this.workers.isEmpty();
  }

  public void attachWorker(final EntityAnimal worker) {
    if(this.workers.size() >= MAX_WORKERS) {
      return;
    }

    worker.clearLeashed(true, false);
    worker.setHomePosAndDistance(this.pos, 3);

    this.workers.push(new WorkerData(worker));
  }

  public void detachWorkers(final EntityPlayer detacher) {
    if(this.workers.isEmpty()) {
      GradientMod.logger.error("Attempted to detach worker, but no worker present ({})", this.pos);
      return;
    }

    for(final WorkerData worker : this.workers) {
      worker.worker.detachHome();
      worker.worker.setLeashHolder(detacher, true);
    }

    this.workers.clear();
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    int workerIndex = 0;
    for(final WorkerData worker : this.workers) {
      final double x = this.pos.getX() + worker.getTargetX(this.workers.size(), workerIndex);
      final double z = this.pos.getZ() + worker.getTargetZ(this.workers.size(), workerIndex);
      final double y = this.pos.getY();

      worker.worker.getNavigator().tryMoveToXYZ(x, y, z, 1.0d);

      workerIndex++;
    }

    if(this.cranking) {
      this.crankTicks++;

      if(this.crankTicks >= 4) {
        this.cranking = false;
        this.crankTicks = 0;
        this.storage.addEnergy(1.0f, false);
        this.markDirty();
      }
    }
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.HAND_CRANK) {
        return facing == state.getValue(BlockHandCrank.FACING);
      }
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.HAND_CRANK) {
        if(facing == state.getValue(BlockHandCrank.FACING)) {
          return STORAGE.cast(this.storage);
        }
      }
    }

    return super.getCapability(capability, facing);
  }

  private static class WorkerData {
    private final EntityAnimal worker;
    private float targetTheta;
    private BlockPos targetPos;

    private WorkerData(final EntityAnimal worker) {
      this.worker = worker;
    }

    private double getTargetX(final int workerCount, final int workerIndex) {
      return Math.cos(this.targetTheta + Math.PI * 2.0d / workerCount * workerIndex) * WORKER_DISTANCE;
    }

    private double getTargetZ(final int workerCount, final int workerIndex) {
      return Math.sin(this.targetTheta + Math.PI * 2.0d / workerCount * workerIndex) * WORKER_DISTANCE;
    }
  }
}
