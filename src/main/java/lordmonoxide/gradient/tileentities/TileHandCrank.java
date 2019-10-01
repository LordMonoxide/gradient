package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockHandCrank;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TileHandCrank extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private static final int MAX_WORKERS = 4;
  private static final double WORKER_DISTANCE = 4.0d;

  private final IKineticEnergyStorage energy = new KineticEnergyStorage(5.0f, 0.0f, 5.0f) {
    @Override
    public void onEnergyChanged() {
      TileHandCrank.this.markDirty();
    }
  };

  private final LinkedList<WorkerData> workers = new LinkedList<>();
  private float workerTargetTheta;

  @Nullable
  private NBTTagList workersDeferredNbt;

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
    if(this.world.isRemote) {
      return;
    }

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
    this.updateTargets();
    this.markDirty();
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
    this.markDirty();
  }

  private boolean areWorkersAtTargets() {
    for(final WorkerData worker : this.workers) {
      if(!worker.isAtTarget()) {
        return false;
      }
    }

    return true;
  }

  private void moveToTargets() {
    for(final WorkerData worker : this.workers) {
      if(!worker.isAtTarget()) {
        worker.moveToTarget();
      }
    }
  }

  private void updateTargets() {
    int workerIndex = 0;
    for(final WorkerData worker : this.workers) {
      worker.updateTarget(workerIndex);
      workerIndex++;
    }
  }

  private void preventEating() {
    for(final WorkerData worker : this.workers) {
      if(worker.worker instanceof AbstractHorse) {
        final AbstractHorse horse = (AbstractHorse)worker.worker;
        if(horse.isEatingHaystack()) {
          horse.setEatingHaystack(false);
        }
      }
    }
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    if(this.workersDeferredNbt != null) {
      this.loadWorkers(this.workersDeferredNbt);
      this.workersDeferredNbt = null;
    }

    if(this.hasWorker()) {
      this.preventEating();
      this.moveToTargets();

      if(this.areWorkersAtTargets()) {
        this.energy.addEnergy(0.25f * this.workers.size(), false);
        this.workerTargetTheta += Math.PI / 4;
        this.updateTargets();
        this.markDirty();
      }
    } else if(this.cranking) {
      this.crankTicks++;

      if(this.crankTicks >= 4) {
        this.cranking = false;
        this.crankTicks = 0;
        this.energy.addEnergy(1.0f, false);
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
          return STORAGE.cast(this.energy);
        }
      }
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    nbt.setTag("Energy", this.energy.serializeNbt());

    final NBTTagList workers = new NBTTagList();
    for(final WorkerData worker : this.workers) {
      workers.appendTag(NBTUtil.createUUIDTag(worker.worker.getUniqueID()));
    }
    nbt.setTag("Workers", workers);

    nbt.setFloat("Theta", this.workerTargetTheta);
    nbt.setInteger("CrankTicks", this.crankTicks);
    nbt.setBoolean("Cranking", this.cranking);

    return super.writeToNBT(nbt);
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    this.workers.clear();

    final NBTTagCompound energy = nbt.getCompoundTag("Energy");
    this.energy.deserializeNbt(energy);

    final NBTTagList workers = nbt.getTagList("Workers", Constants.NBT.TAG_COMPOUND);
    if(this.world != null) {
      this.loadWorkers(workers);
    } else {
      this.workersDeferredNbt = workers;
    }

    this.workerTargetTheta = nbt.getFloat("Theta");
    this.crankTicks = nbt.getInteger("CrankTicks");
    this.cranking = nbt.getBoolean("Cranking");

    super.readFromNBT(nbt);
  }

  private void loadWorkers(final NBTTagList workers) {
    for(int i = 0; i < workers.tagCount(); i++) {
      final UUID uuid = NBTUtil.getUUIDFromTag(workers.getCompoundTagAt(i));
      final List<EntityAnimal> animals = this.world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos.getX() - 15.0d, this.pos.getY() - 15.0d, this.pos.getZ() - 15.0d, this.pos.getX() + 15.0d, this.pos.getY() + 15.0d, this.pos.getZ() + 15.0d), e -> e.getUniqueID().equals(uuid));

      if(!animals.isEmpty()) {
        this.attachWorker(animals.get(0));
      }
    }
  }

  private final class WorkerData {
    private final EntityAnimal worker;
    private Vec3d targetPos;
    private AxisAlignedBB targetBb;

    private WorkerData(final EntityAnimal worker) {
      this.worker = worker;
    }

    private double getTargetX(final int workerIndex) {
      return Math.cos(TileHandCrank.this.workerTargetTheta + Math.PI * 2.0d / TileHandCrank.this.workers.size() * workerIndex) * WORKER_DISTANCE;
    }

    private double getTargetZ(final int workerIndex) {
      return Math.sin(TileHandCrank.this.workerTargetTheta + Math.PI * 2.0d / TileHandCrank.this.workers.size() * workerIndex) * WORKER_DISTANCE;
    }

    private void updateTarget(final int workerIndex) {
      final double x = TileHandCrank.this.pos.getX() + this.getTargetX(workerIndex);
      final double z = TileHandCrank.this.pos.getZ() + this.getTargetZ(workerIndex);
      final double y = TileHandCrank.this.pos.getY();
      this.targetPos = new Vec3d(x, y, z);
      this.targetBb = new AxisAlignedBB(x - 0.5d, y - 0.5d, z - 0.5d, x + 0.5d, y + 0.5d, z + 0.5d);
    }

    private void moveToTarget() {
      this.worker.getNavigator().tryMoveToXYZ(this.targetPos.x, this.targetPos.y, this.targetPos.z, 1.0d);
    }

    private boolean isAtTarget() {
      if(this.targetBb == null) {
        return true;
      }

      return this.worker.getEntityBoundingBox().intersects(this.targetBb);
    }
  }
}
