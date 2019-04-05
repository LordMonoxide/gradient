package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.DryingRecipe;
import lordmonoxide.gradient.recipes.GradientRecipeTypes;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileDryingRack extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private final ItemStackHandler inventory = new ItemStackHandler(1);

  @Nullable
  private DryingRecipe recipe;
  private Age age = Age.AGE1;
  private int ticks;

  public TileDryingRack() {
    super(GradientTileEntities.DRYING_RACK);
  }

  public boolean hasItem() {
    return !this.getItem().isEmpty();
  }

  public ItemStack getItem() {
    return this.inventory.getStackInSlot(0);
  }

  public ItemStack takeItem() {
    final ItemStack input = this.inventory.extractItem(0, this.inventory.getSlotLimit(0), false);
    this.recipe = null;
    this.ticks = 0;
    this.sync();
    return input;
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasItem()) {
      this.age = AgeUtils.getPlayerAge(player);

      final ItemStack input = stack.split(1);
      this.inventory.setStackInSlot(0, input);

      this.updateRecipe();
      this.ticks = 0;

      this.sync();
      return stack;
    }

    return stack;
  }

  @Override
  public void tick() {
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
    this.recipe = RecipeUtils.findRecipe(GradientRecipeTypes.DRYING, recipe -> recipe.matches(this.inventory, this.age, 0, 0));
  }

  @Override
  public NBTTagCompound write(final NBTTagCompound compound) {
    compound.put("inventory", this.inventory.serializeNBT());
    compound.putInt("player_age", this.age.value());
    compound.putInt("ticks", this.ticks);
    return super.write(compound);
  }

  @Override
  public void read(final NBTTagCompound compound) {
    final NBTTagCompound inv = compound.getCompound("inventory");
    inv.remove("Size");
    this.inventory.deserializeNBT(inv);

    final int age = compound.getInt("player_age");

    try {
      this.age = Age.get(age);
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.warn("Invalid age in {}: {}", this, age);
    }

    this.ticks = compound.getInt("ticks");

    this.updateRecipe();

    super.read(compound);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    return super.getCapability(capability, facing);
  }

  protected void sync() {
    if(!this.world.isRemote) {
      final IBlockState state = this.world.getBlockState(this.getPos());
      this.world.notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.write(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.read(pkt.getNbtCompound());
  }
}
