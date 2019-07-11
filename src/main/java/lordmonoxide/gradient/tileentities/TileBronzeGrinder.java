package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.recipes.GrinderRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBronzeGrinder extends TileEntity implements ITickableTileEntity {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  private static final Fluid STEAM = null; //TODO FluidRegistry.getFluid(FluidName.steam.getName());

  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  public static final int WORKING_SLOT = 2;
  public static final int TOTAL_SLOTS_COUNT = 3;

  private static final int WORK_TIME = 600;
  private static final int STEAM_USE_PER_TICK = 4;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);

  public final FluidTank tankSteam = new FluidTank(Fluid.BUCKET_VOLUME * 16);
  private final FluidHandlerFluidMap tanks = new FluidHandlerFluidMap() {
    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
      final int amount = super.fill(resource, doFill);

      if(amount != 0) {
        TileBronzeGrinder.this.sync();
      }

      return amount;
    }
  };

  private int workTicks;

  public TileBronzeGrinder() {
    super(GradientTileEntities.BRONZE_GRINDER);
    this.tanks.addHandler(STEAM, this.tankSteam);
  }

  public boolean useBucket(final PlayerEntity player, final Hand hand, final World world, final BlockPos pos, final Direction side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      this.sync();
      return true;
    }

    return false;
  }

  @Override
  public void tick() {
    this.work();
  }

  public boolean isWorking() {
    return !this.inventory.getStackInSlot(WORKING_SLOT).isEmpty();
  }

  private boolean isWorkComplete() {
    return this.workTicks >= WORK_TIME;
  }

  public float getWorkPercent() {
    return (float)this.workTicks / WORK_TIME;
  }

  private ItemStack getInputStack() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }

  private void work() {
    if(!this.isWorking() && !this.getInputStack().isEmpty()) {
      final ItemStack finished = GrinderRecipes.getOutput(this.getInputStack()).copy();

      if(this.inventory.insertItem(OUTPUT_SLOT, finished, true).isEmpty()) {
        this.inventory.extractItem(INPUT_SLOT, 1, false);
        this.inventory.setStackInSlot(WORKING_SLOT, finished);
        this.workTicks = 0;
      }
    }

    if(this.isWorking()) {
      if(this.tankSteam.drain(STEAM_USE_PER_TICK, true).amount >= STEAM_USE_PER_TICK) {
        this.workTicks++;
      }

      if(this.isWorkComplete()) {
        final ItemStack cooked = this.inventory.extractItem(WORKING_SLOT, 1, false);
        this.inventory.insertItem(OUTPUT_SLOT, cooked, false);
      }
    }
  }

  @Override
  public CompoundNBT write(final CompoundNBT compound) {
    compound.put("inventory", this.inventory.serializeNBT());
    compound.put("steam", this.tankSteam.writeToNBT(new CompoundNBT()));
    compound.putInt("workTicks", this.workTicks);
    return super.write(compound);
  }

  @Override
  public void read(final CompoundNBT compound) {
    final CompoundNBT inv = compound.getCompound("inventory");
    inv.remove("Size");
    this.inventory.deserializeNBT(inv);

    this.tankSteam.readFromNBT(compound.getCompound("steam"));
    this.workTicks = compound.getInt("workTicks");
    super.read(compound);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    if(capability == FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.tanks);
    }

    return super.getCapability(capability, facing);
  }

  private void sync() {
    if(!this.world.isRemote) {
      final BlockState state = this.world.getBlockState(this.getPos());
      this.world.notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
    this.read(pkt.getNbtCompound());
  }
}
