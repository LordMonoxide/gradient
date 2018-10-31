package lordmonoxide.gradient.blocks.manualgrinder;

import buildcraft.lib.misc.CraftingUtil;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.recipes.RecipeHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileManualGrinder extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private final ContainerManualGrinder container = new ContainerManualGrinder();
  private final InventoryCrafting crafting = new InventoryCrafting(this.container, 1, 1);
  private final ItemStackHandler inventory = new ItemStackHandler(2);

  @Nullable
  private GrindingRecipe recipe;
  private int passes;
  private int ticks;

  public boolean hasInput() {
    return !this.inventory.getStackInSlot(0).isEmpty();
  }

  public boolean hasOutput() {
    return !this.inventory.getStackInSlot(1).isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(0);
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(1);
  }

  public ItemStack takeInput() {
    this.recipe = null;
    final ItemStack input = this.inventory.extractItem(0, this.inventory.getSlotLimit(0), false);
    this.sync();
    return input;
  }

  public ItemStack takeOutput() {
    final ItemStack output = this.inventory.extractItem(1, this.inventory.getSlotLimit(1), false);
    this.sync();
    return output;
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasInput()) {
      this.recipe = this.findRecipe(stack, RecipeHelper.getPlayerAge(player));
      this.passes = 0;

      if(this.recipe == null) {
        return stack;
      }

      this.ticks = this.recipe.ticks;
    }

    final ItemStack remaining = this.inventory.insertItem(0, stack, false);
    this.sync();
    return remaining;
  }

  @Override
  public void update() {
    if(this.world.isRemote) {
      return;
    }

    if(this.recipe == null) {
      return;
    }

    if(this.ticks < this.recipe.ticks) {
      this.ticks++;
      this.markDirty();
    }

    if(this.ticks >= this.recipe.ticks && this.passes >= this.recipe.passes) {
      this.passes = 0;

      this.inventory.extractItem(0, 1, false);
      this.inventory.insertItem(1, this.recipe.getCraftingResult(this.crafting), false);

      if(!this.hasInput()) {
        this.recipe = null;
      }

      this.sync();
    }
  }

  public void crank() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks >= this.recipe.ticks) {
      ((WorldServer)this.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.5d, this.pos.getY() + 0.5d, this.pos.getZ() + 0.5d, 10, 0.1d, 0.1d, 0.1d, 0.01d);

      this.ticks = 0;
      this.passes++;

      this.markDirty();
    }
  }

  @Nullable
  private GrindingRecipe findRecipe(final ItemStack input, final Age age) {
    this.container.setPlayerAge(age);
    this.crafting.setInventorySlotContents(0, input);
    final IRecipe recipe = CraftingUtil.findMatchingRecipe(this.crafting, this.world);

    if(!(recipe instanceof GrindingRecipe)) {
      return null;
    }

    return (GrindingRecipe)recipe;
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setInteger("passes", this.passes);
    compound.setInteger("ticks", this.ticks);
    compound.setInteger("player_age", this.container.getPlayerAge().value());

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);
    this.passes = compound.getInteger("passes");
    this.ticks = compound.getInteger("ticks");
    this.container.setPlayerAge(Age.get(compound.getInteger("player_age")));

    this.recipe = this.findRecipe(this.getInput(), this.container.getPlayerAge());

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

  private void sync() {
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
