package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBronzeOven extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  public static final int COOKING_SLOT = 2;
  public static final int TOTAL_SLOTS_COUNT = 3;

  private static final int COOK_TIME = 400;
  private static final int STEAM_USE_PER_TICK = 4;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT) {
    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
      if(slot != INPUT_SLOT) {
        return false;
      }

      return super.isItemValid(slot, stack) && !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if(!this.isItemValid(slot, stack) && !TileBronzeOven.this.forceInsert) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
      if(slot == COOKING_SLOT) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
      super.onContentsChanged(slot);

      if(slot == COOKING_SLOT) {
        if(!this.getStackInSlot(slot).isEmpty()) {
          TileBronzeOven.this.cookTicks = 0;
        }
      }

      TileBronzeOven.this.sync();
    }
  };

  public final FluidTank tankSteam = new FluidTank(Fluid.BUCKET_VOLUME * 16) {
    private final Fluid steam = FluidRegistry.getFluid("steam");

    @Override
    public boolean canFillFluidType(final FluidStack fluid) {
      return super.canFillFluidType(fluid) && fluid.getFluid() == this.steam;
    }

    @Override
    protected void onContentsChanged() {
      super.onContentsChanged();
      TileBronzeOven.this.sync();
    }
  };

  private int cookTicks;
  private boolean forceInsert;

  public boolean useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    return FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
  }

  @Override
  public void update() {
    this.cook();
  }

  public boolean isCooking() {
    return !this.inventory.getStackInSlot(COOKING_SLOT).isEmpty();
  }

  private boolean isCooked() {
    return this.cookTicks >= COOK_TIME;
  }

  public float getCookPercent() {
    return (float)this.cookTicks / COOK_TIME;
  }

  private ItemStack getInputStack() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }

  private void cook() {
    if(!this.isCooking() && !this.getInputStack().isEmpty()) {
      final ItemStack cooked = FurnaceRecipes.instance().getSmeltingResult(this.getInputStack()).copy();

      this.forceInsert = true;
      if(this.inventory.insertItem(OUTPUT_SLOT, cooked, true).isEmpty()) {
        this.inventory.extractItem(INPUT_SLOT, 1, false);
        this.inventory.setStackInSlot(COOKING_SLOT, cooked);
      }
      this.forceInsert = false;
    }

    if(this.isCooking()) {
      final FluidStack steam = this.tankSteam.drain(STEAM_USE_PER_TICK, true);

      if(steam != null && steam.amount >= STEAM_USE_PER_TICK) {
        this.cookTicks++;
      }

      if(this.isCooked()) {
        final ItemStack cooked = this.inventory.extractItem(COOKING_SLOT, this.inventory.getSlotLimit(COOKING_SLOT), false);
        this.forceInsert = true;
        this.inventory.insertItem(OUTPUT_SLOT, cooked, false);
        this.forceInsert = false;
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("steam", this.tankSteam.writeToNBT(new NBTTagCompound()));
    compound.setInteger("cookTicks", this.cookTicks);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    this.tankSteam.readFromNBT(compound.getCompoundTag("steam"));
    this.cookTicks = compound.getInteger("cookTicks");
    super.readFromNBT(compound);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ITEM_HANDLER_CAPABILITY ||
      capability == FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inventory);
    }

    if(capability == FLUID_HANDLER_CAPABILITY) {
      return FLUID_HANDLER_CAPABILITY.cast(this.tankSteam);
    }

    return super.getCapability(capability, facing);
  }

  private void sync() {
    if(!this.getWorld().isRemote) {
      WorldUtils.notifyUpdate(this.world, this.pos);
      this.markDirty();
    }
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}
