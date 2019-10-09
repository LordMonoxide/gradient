package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.BlockFlywheel;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileFlywheel extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage energy = new KineticEnergyStorage(10000.0f);

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).queueConnection(this.pos, this);
  }

  public float getEnergy() {
    return this.energy.getEnergy();
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.FLYWHEEL) {
        final EnumFacing myFacing = state.getValue(BlockFlywheel.FACING);
        return facing == myFacing.rotateY() || facing == myFacing.rotateYCCW();
      }
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.FLYWHEEL) {
        final EnumFacing myFacing = state.getValue(BlockFlywheel.FACING);

        if(facing == myFacing.rotateY() || facing == myFacing.rotateYCCW()) {
          return STORAGE.cast(this.energy);
        }
      }
    }

    return super.getCapability(capability, facing);
  }
}
