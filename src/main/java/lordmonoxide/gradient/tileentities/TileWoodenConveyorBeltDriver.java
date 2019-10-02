package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockWoodenConveyorBelt;
import lordmonoxide.gradient.blocks.BlockWoodenConveyorBeltDriver;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileWoodenConveyorBeltDriver extends TileEntity {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage node = new KineticEnergyStorage(1.0f, 1.0f, 0.0f);

  private final Map<EnumFacing, List<IBlockState>> belts = new EnumMap<>(EnumFacing.class);

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).connect(this.pos, this);

    for(final EnumFacing side : EnumFacing.HORIZONTALS) {
      if(this.world.getBlockState(this.pos.offset(side)).getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT) {
        this.addBelt(side);
      }
    }
  }

  public void remove() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).disconnect(this.pos);
  }

  public void addBelt(final EnumFacing side) {
    GradientMod.logger.info("Adding {} for {}", side, this.pos);

    BlockPos beltPos = this.pos.offset(side);
    IBlockState belt = this.world.getBlockState(beltPos);
    final EnumFacing beltFacing = belt.getValue(BlockWoodenConveyorBelt.FACING);
    final List<IBlockState> beltParts = this.belts.computeIfAbsent(side, key -> new ArrayList<>());

    while(belt.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT && belt.getValue(BlockWoodenConveyorBelt.FACING) == beltFacing) {
      beltParts.add(belt);
      GradientMod.logger.info("Added {}", beltPos);
      beltPos = beltPos.offset(beltFacing);
      belt = this.world.getBlockState(beltPos);
    }

    beltPos = this.pos.offset(side).offset(beltFacing.getOpposite());
    belt = this.world.getBlockState(beltPos);

    while(belt.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT && belt.getValue(BlockWoodenConveyorBelt.FACING) == beltFacing) {
      beltParts.add(belt);
      GradientMod.logger.info("Added {}", beltPos);
      beltPos = beltPos.offset(beltFacing.getOpposite());
      belt = this.world.getBlockState(beltPos);
    }
  }

  public void removeBelt(final EnumFacing side) {
    this.belts.computeIfAbsent(side, key -> new ArrayList<>()).clear();

    GradientMod.logger.info("Removed {}", side);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT_DRIVER && state.getValue(BlockWoodenConveyorBeltDriver.FACING) == facing) {
        return true;
      }
    }

    return super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE && this.hasCapability(capability, facing)) {
      return STORAGE.cast(this.node);
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    nbt.setTag("Energy", this.node.serializeNbt());
    return super.writeToNBT(nbt);
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    final NBTTagCompound energy = nbt.getCompoundTag("Energy");
    this.node.deserializeNbt(energy);
    super.readFromNBT(nbt);
  }
}
