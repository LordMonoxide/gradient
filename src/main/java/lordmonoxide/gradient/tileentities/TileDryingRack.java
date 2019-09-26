package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.DryingRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
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

import javax.annotation.Nullable;

public class TileDryingRack extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private final ItemStackHandler inventory = new ItemStackHandler(1) {
    @Override
    public int getSlotLimit(final int slot) {
      return 1;
    }

    @Override
    protected void onContentsChanged(final int slot) {
      super.onContentsChanged(slot);

      final ItemStack stack = this.getStackInSlot(slot);

      if(stack.isEmpty()) {
        TileDryingRack.this.recipe = null;
      } else {
        TileDryingRack.this.updateRecipe();
      }

      TileDryingRack.this.ticks = 0;
      TileDryingRack.this.sync();
    }
  };

  @Nullable
  private DryingRecipe recipe;
  private Age age = Age.AGE1;
  private int ticks;

  public boolean hasItem() {
    return !this.getItem().isEmpty();
  }

  public ItemStack getItem() {
    return this.inventory.getStackInSlot(0);
  }

  public ItemStack takeItem() {
    return this.inventory.extractItem(0, this.inventory.getSlotLimit(0), false);
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasItem()) {
      this.age = AgeUtils.getPlayerAge(player);

      final ItemStack input = stack.splitStack(1);
      this.inventory.setStackInSlot(0, input);

      return stack;
    }

    return stack;
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    this.dry();
  }

  private void dry() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks < this.recipe.ticks) {
      this.ticks++;
      this.markDirty();
    }

    if(this.ticks >= this.recipe.ticks) {
      this.inventory.setStackInSlot(0, this.recipe.getRecipeOutput().copy());
      this.recipe = null;
      this.sync();
    }
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(DryingRecipe.class, recipe -> recipe.matches(this.inventory, this.age, 0, 0));
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setInteger("player_age", this.age.value());
    compound.setInteger("ticks", this.ticks);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    final int age = compound.getInteger("player_age");

    try {
      this.age = Age.get(age);
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.warn("Invalid age in {}: {}", this, age);
    }

    this.ticks = compound.getInteger("ticks");

    this.updateRecipe();

    super.readFromNBT(compound);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ITEM_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inventory);
    }

    return super.getCapability(capability, facing);
  }

  protected void sync() {
    if(!this.getWorld().isRemote) {
      final IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
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
