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
import net.minecraft.util.math.Vec3d;
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
  private static final double WORKER_DISTANCE = 4.0d;

  private final IKineticEnergyStorage storage = new KineticEnergyStorage(5.0f, 0.0f, 5.0f);

  private final LinkedList<WorkerData> workers = new LinkedList<>();
  private float workerTargetTheta;

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
    this.updateWorkerPositionsForce();
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

  private void updateWorkerPositionsForce() {
    int workerIndex = 0;
    for(final WorkerData worker : this.workers) {
      worker.moveToTarget(workerIndex);
      workerIndex++;
    }
  }

  private void updateWorkerPositions() {
    int workerIndex = 0;
    for(final WorkerData worker : this.workers) {
      if(!worker.isAtTarget()) {
        worker.moveToTarget(workerIndex);
      }

      workerIndex++;
    }
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    this.updateWorkerPositions();

    if(!this.hasWorker() && this.cranking) {
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

  private class WorkerData {
    private final EntityAnimal worker;
    private Vec3d targetPos;

    private WorkerData(final EntityAnimal worker) {
      this.worker = worker;
    }

    private double getTargetX(final int workerIndex) {
      return Math.cos(TileHandCrank.this.workerTargetTheta + Math.PI * 2.0d / TileHandCrank.this.workers.size() * workerIndex) * WORKER_DISTANCE;
    }

    private double getTargetZ(final int workerIndex) {
      return Math.sin(TileHandCrank.this.workerTargetTheta + Math.PI * 2.0d / TileHandCrank.this.workers.size() * workerIndex) * WORKER_DISTANCE;
    }

    private void moveToTarget(final int workerIndex) {
      final double x = TileHandCrank.this.pos.getX() + this.getTargetX(workerIndex);
      final double z = TileHandCrank.this.pos.getZ() + this.getTargetZ(workerIndex);
      final double y = TileHandCrank.this.pos.getY();

      this.worker.getNavigator().tryMoveToXYZ(x, y, z, 1.0d);
      this.targetPos = new Vec3d(x, y, z);
    }

    private boolean isAtTarget() {
      if(this.targetPos == null) {
        return true;
      }

      return this.worker.getEntityBoundingBox().intersectsWithXZ(this.targetPos);
    }
  }
}
