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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class TileHandCrank extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage storage = new KineticEnergyStorage(5.0f, 0.0f, 5.0f);

  @Nullable
  private EntityAnimal worker;
  private float workerTargetTheta;
  private BlockPos workerTargetPos;

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
    return this.worker != null;
  }

  public void attachWorker(final EntityPlayer attacher, final EntityAnimal worker) {
    if(this.hasWorker()) {
      ItemHandlerHelper.giveItemToPlayer(attacher, new ItemStack(Items.LEAD));
      this.detachWorker(attacher);
    }

    this.worker = worker;

    worker.clearLeashed(true, false);
    worker.setHomePosAndDistance(this.pos, 3);
  }

  public void detachWorker(final EntityPlayer detacher) {
    if(this.worker == null) {
      GradientMod.logger.error("Attempted to detach worker, but no worker present ({})", this.pos);
      return;
    }

    this.worker.detachHome();
    this.worker.setLeashHolder(detacher, true);
    this.worker = null;
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    if(this.worker != null) {
      final double x = this.pos.getX() + MathHelper.cos(this.workerTargetTheta) * 3.0d;
      final double z = this.pos.getZ() + MathHelper.sin(this.workerTargetTheta) * 3.0d;
      final double y = this.pos.getY();

      this.worker.getNavigator().tryMoveToXYZ(x, y, z, 1.0d);
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
}
