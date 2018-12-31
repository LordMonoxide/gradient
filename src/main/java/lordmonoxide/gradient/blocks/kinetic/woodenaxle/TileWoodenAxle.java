package lordmonoxide.gradient.blocks.kinetic.woodenaxle;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyTransfer;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

public class TileWoodenAxle extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyTransfer transfer = new KineticEnergyTransfer();

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).connect(this.pos, this);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == TRANSFER) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.WOODEN_AXLE) {
        return facing != null && facing.getAxis() == state.getValue(BlockRotatedPillar.AXIS);
      }
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == TRANSFER) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.WOODEN_AXLE) {
        if(facing != null && facing.getAxis() == state.getValue(BlockRotatedPillar.AXIS)) {
          return TRANSFER.cast(this.transfer);
        }
      }
    }

    return super.getCapability(capability, facing);
  }
}
