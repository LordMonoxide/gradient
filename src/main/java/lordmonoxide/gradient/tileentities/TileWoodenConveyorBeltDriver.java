package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockWoodenConveyorBelt;
import lordmonoxide.gradient.blocks.BlockWoodenConveyorBeltDriver;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class TileWoodenConveyorBeltDriver extends TileEntity implements ITickable {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private final IKineticEnergyStorage node = new KineticEnergyStorage(1.0f, 1.0f, 0.0f) {
    @Override
    public void onEnergyChanged() {
      GradientMod.logger.info("New energy: {}", this.getEnergy());
    }
  };

  private final Map<EnumFacing, List<TileWoodenConveyorBelt>> belts = new EnumMap<>(EnumFacing.class);
  private final Map<EnumFacing, AxisAlignedBB> movingBoxes = new EnumMap<>(EnumFacing.class);
  private int beltCount;

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

  public void onRemove() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).disconnect(this.pos);
  }

  public void addBelt(final EnumFacing side) {
    GradientMod.logger.info("Adding {} for {}", side, this.pos);

    BlockPos beltPos = this.pos.offset(side);
    IBlockState belt = this.world.getBlockState(beltPos);
    TileWoodenConveyorBelt te = WorldUtils.getTileEntity(this.world, beltPos, TileWoodenConveyorBelt.class);

    final EnumFacing beltFacing = belt.getValue(BlockWoodenConveyorBelt.FACING);
    final List<TileWoodenConveyorBelt> beltParts = this.belts.computeIfAbsent(side, key -> new ArrayList<>());

    double minX = Double.MAX_VALUE;
    double maxX = -Double.MAX_VALUE;
    double minZ = Double.MAX_VALUE;
    double maxZ = -Double.MAX_VALUE;

    while(te != null && belt.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT && belt.getValue(BlockWoodenConveyorBelt.FACING) == beltFacing) {
      final AxisAlignedBB collisionBox = belt.getCollisionBoundingBox(this.world, beltPos);

      if(minX > collisionBox.minX + beltPos.getX()) {
        minX = collisionBox.minX + beltPos.getX();
      }

      if(maxX < collisionBox.maxX + beltPos.getX()) {
        maxX = collisionBox.maxX + beltPos.getX();
      }

      if(minZ > collisionBox.minZ + beltPos.getZ()) {
        minZ = collisionBox.minZ + beltPos.getZ();
      }

      if(maxZ < collisionBox.maxZ + beltPos.getZ()) {
        maxZ = collisionBox.maxZ + beltPos.getZ();
      }

      beltParts.add(te);
      te.addDriver(this, side);
      GradientMod.logger.info("Added {}", beltPos);
      beltPos = beltPos.offset(beltFacing);
      belt = this.world.getBlockState(beltPos);
      te = WorldUtils.getTileEntity(this.world, beltPos, TileWoodenConveyorBelt.class);
      GradientMod.logger.info("Next {} {} {}", beltPos, belt, te);
    }

    beltPos = this.pos.offset(side).offset(beltFacing.getOpposite());
    belt = this.world.getBlockState(beltPos);
    te = WorldUtils.getTileEntity(this.world, beltPos, TileWoodenConveyorBelt.class);

    while(te != null && belt.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT && belt.getValue(BlockWoodenConveyorBelt.FACING) == beltFacing) {
      final AxisAlignedBB collisionBox = belt.getCollisionBoundingBox(this.world, beltPos);

      if(minX > collisionBox.minX + beltPos.getX()) {
        minX = collisionBox.minX + beltPos.getX();
      }

      if(maxX < collisionBox.maxX + beltPos.getX()) {
        maxX = collisionBox.maxX + beltPos.getX();
      }

      if(minZ > collisionBox.minZ + beltPos.getZ()) {
        minZ = collisionBox.minZ + beltPos.getZ();
      }

      if(maxZ < collisionBox.maxZ + beltPos.getZ()) {
        maxZ = collisionBox.maxZ + beltPos.getZ();
      }

      beltParts.add(te);
      te.addDriver(this, side);
      GradientMod.logger.info("Added {}", beltPos);
      beltPos = beltPos.offset(beltFacing.getOpposite());
      belt = this.world.getBlockState(beltPos);
      te = WorldUtils.getTileEntity(this.world, beltPos, TileWoodenConveyorBelt.class);
      GradientMod.logger.info("Next {} {} {}", beltPos, belt, te);
    }

    if(!beltParts.isEmpty()) {
      this.beltCount += beltParts.size();
      this.movingBoxes.put(side, new AxisAlignedBB(minX, this.pos.getY(), minZ, maxX, this.pos.getY() + 1.0d, maxZ));

      GradientMod.logger.info("Setting bb for {} to {}", side, this.movingBoxes.get(side));
    }
  }

  public void removeBelt(final EnumFacing side) {
    final List<TileWoodenConveyorBelt> belts = this.belts.computeIfAbsent(side, key -> new ArrayList<>());

    for(final TileWoodenConveyorBelt belt : belts) {
      belt.removeDriver(this);
    }

    this.beltCount -= belts.size();
    belts.clear();

    GradientMod.logger.info("Removed {}", side);
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    if(this.node.getEnergy() < 0.0001f) {
      return;
    }

    final float neededEnergy = 0.005f * this.beltCount;
    final float extractedEnergy = this.node.removeEnergy(neededEnergy, false);
    final double beltSpeedModifier = extractedEnergy / neededEnergy;

    GradientMod.logger.info("{}/{}: {}", extractedEnergy, neededEnergy, beltSpeedModifier);

    for(final EnumFacing side : EnumFacing.HORIZONTALS) {
      final List<TileWoodenConveyorBelt> belts = this.belts.computeIfAbsent(side, key -> new ArrayList<>());

      if(!belts.isEmpty()) {
        final TileWoodenConveyorBelt belt = belts.get(0);
        final EnumFacing beltFacing = belt.getFacing();

        for(final Entity entity : this.world.getEntitiesWithinAABB(Entity.class, this.movingBoxes.get(side))) {
          if(beltFacing.getXOffset() != 0) {
            entity.motionX = beltFacing.getXOffset() * 0.05d * beltSpeedModifier;
            entity.velocityChanged = true;
            GradientMod.logger.info("Setting {} motionX to {}", entity, entity.motionX);
          }

          if(beltFacing.getZOffset() != 0) {
            entity.motionZ = beltFacing.getZOffset() * 0.05d * beltSpeedModifier;
            entity.velocityChanged = true;
            GradientMod.logger.info("Setting {} motionZ to {}", entity, entity.motionZ);
          }
        }
      }
    }
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
