package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.BlockWoodenConveyorBeltDriver;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileAutomaticGrindstone extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private static final int INPUT_SLOT = 0;
  private static final int OUTPUT_SLOT = 1;

  private final IKineticEnergyStorage node = new KineticEnergyStorage(1.0f, 1.0f, 0.0f);

  private final ItemStackHandler inventory = new ItemStackHandler(2) {
    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
      if(slot == INPUT_SLOT) {
        return RecipeUtils.findRecipe(GrindingRecipe.class, recipe -> recipe.matches(stack)) != null;
      }

      return false;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if(!this.isItemValid(slot, stack) && !TileAutomaticGrindstone.this.forceInsert) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
      if(slot == INPUT_SLOT) {
        final ItemStack stack = this.getStackInSlot(slot);

        if(!stack.isEmpty()) {
          if(TileAutomaticGrindstone.this.recipe == null) {
            TileAutomaticGrindstone.this.ticks = 0;
            TileAutomaticGrindstone.this.updateRecipe();

            if(TileAutomaticGrindstone.this.recipe != null) {
              TileAutomaticGrindstone.this.maxTicks = TileAutomaticGrindstone.this.recipe.ticks * TileAutomaticGrindstone.this.recipe.passes * 0.9f;
            }
          }
        } else {
          TileAutomaticGrindstone.this.recipe = null;
          TileAutomaticGrindstone.this.ticks = 0;
        }
      }

      TileAutomaticGrindstone.this.sync();
    }
  };

  @Nullable
  private GrindingRecipe recipe;
  private Age age = Age.AGE1;
  private float ticks;
  private float maxTicks;
  private boolean forceInsert;

  public boolean hasInput() {
    return !this.inventory.getStackInSlot(INPUT_SLOT).isEmpty();
  }

  public boolean hasOutput() {
    return !this.inventory.getStackInSlot(OUTPUT_SLOT).isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(OUTPUT_SLOT);
  }

  public ItemStack takeInput() {
    return this.inventory.extractItem(INPUT_SLOT, this.inventory.getSlotLimit(INPUT_SLOT), false);
  }

  public ItemStack takeOutput() {
    return this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(OUTPUT_SLOT), false);
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasInput()) {
      this.age = AgeUtils.getPlayerAge(player);
      return this.inventory.insertItem(INPUT_SLOT, stack, false);
    }

    return this.inventory.insertItem(INPUT_SLOT, stack, false);
  }

  @Override
  public void onLoad() {
    if(this.world.isRemote) {
      return;
    }

    EnergyNetworkManager.getManager(this.world, STORAGE, TRANSFER).queueConnection(this.pos, this);
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    if(this.recipe == null || this.node.getEnergy() < 0.0001f) {
      return;
    }

    //TODO use energy

    if(this.ticks < this.maxTicks) {
      this.ticks++;
      this.markDirty();
    }

    if(this.ticks >= this.maxTicks) {
      final ItemStack output = this.recipe.getRecipeOutput().copy();
      this.inventory.extractItem(INPUT_SLOT, 1, false);
      this.forceInsert = true;
      this.inventory.insertItem(OUTPUT_SLOT, output, false);
      this.forceInsert = false;
    }
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(GrindingRecipe.class, recipe -> recipe.matches(this.getInput(), this.age));
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
    nbt.setTag("Energy", this.node.serializeNbt());
    nbt.setTag("Inventory", this.inventory.serializeNBT());
    nbt.setFloat("Ticks", this.ticks);
    nbt.setInteger("PlayerAge", this.age.value());

    return super.writeToNBT(nbt);
  }

  @Override
  public void readFromNBT(final NBTTagCompound nbt) {
    final NBTTagCompound energy = nbt.getCompoundTag("Energy");
    this.node.deserializeNbt(energy);

    final NBTTagCompound inv = nbt.getCompoundTag("Inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    this.ticks = nbt.getFloat("Ticks");
    this.age = Age.get(nbt.getInteger("PlayerAge"));

    this.updateRecipe();

    super.readFromNBT(nbt);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE) {
      final IBlockState state = this.world.getBlockState(this.pos);

      if(state.getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT_DRIVER && state.getValue(BlockWoodenConveyorBeltDriver.FACING) == facing) {
        return true;
      }
    }

    return
      capability == ITEM_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == STORAGE && this.hasCapability(capability, facing)) {
      return STORAGE.cast(this.node);
    }

    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inventory);
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
