package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.BlockWoodenConveyorBelt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

//TODO: still need to allow adding more at runtime
public class TileWoodenConveyorBelt extends TileEntity {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private final Set<TileWoodenConveyorBeltDriver> drivers = new HashSet<>();
  private EnumFacing facing;

  private final IItemHandler inv = new DummyItemHandler();

  public EnumFacing getFacing() {
    return this.facing;
  }

  public void addDriver(final TileWoodenConveyorBeltDriver driver) {
    this.drivers.add(driver);
  }

  public void removeDriver(final TileWoodenConveyorBeltDriver driver) {
    this.drivers.remove(driver);
  }

  @Override
  public void onLoad() {
    super.onLoad();

    this.facing = this.world.getBlockState(this.pos).getValue(BlockWoodenConveyorBelt.FACING);
  }

  public void onRemove() {
    for(final TileWoodenConveyorBeltDriver driver : this.drivers) {
      //TODO
//      driver.removeBelt();
//      driver.addBelt();
    }
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return capability == ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inv);
    }

    return super.getCapability(capability, facing);
  }

  private class DummyItemHandler implements IItemHandler {
    @Override
    public int getSlots() {
      return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(final int slot) {
      return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      final World world = TileWoodenConveyorBelt.this.world;

      if(!world.isRemote && !simulate) {
        final BlockPos pos = TileWoodenConveyorBelt.this.pos;
        final EntityItem entity = new EntityItem(world, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5f, stack.copy());
        entity.motionX = 0.0d;
        entity.motionY = 0.0d;
        entity.motionZ = 0.0d;
        world.spawnEntity(entity);
      }

      return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
      return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(final int slot) {
      return 64;
    }
  }
}
