package lordmonoxide.gradient.blocks.mixingbasin;

import buildcraft.lib.misc.CraftingUtil;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.MixingRecipe;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileMixingBasin extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  private static final Fluid WATER = FluidRegistry.getFluid("water");

  private final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME);
  private final FluidHandlerFluidMap tanks = new FluidHandlerFluidMap() {
    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
      final int filled = super.fill(resource, doFill);
      TileMixingBasin.this.updateRecipe(TileMixingBasin.this.container.getPlayerAge());
      TileMixingBasin.this.sync();
      return filled;
    }

    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
      final FluidStack drained = super.drain(resource, doDrain);
      TileMixingBasin.this.updateRecipe(TileMixingBasin.this.container.getPlayerAge());
      TileMixingBasin.this.sync();
      return drained;
    }

    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
      final FluidStack drained = super.drain(maxDrain, doDrain);
      TileMixingBasin.this.updateRecipe(TileMixingBasin.this.container.getPlayerAge());
      TileMixingBasin.this.sync();
      return drained;
    }
  };

  public static final int INPUT_SIZE = 5;
  private static final int OUTPUT_SLOT = INPUT_SIZE;

  private final ContainerMixingBasin container = new ContainerMixingBasin();
  private final InventoryCrafting crafting = new InventoryCrafting(this.container, INPUT_SIZE + 1, 1);
  private final ItemStackHandler inventory = new ItemStackHandler(INPUT_SIZE + 1);

  @Nullable
  private MixingRecipe recipe;
  private int passes;
  private int ticks;

  public TileMixingBasin() {
    this.tanks.addHandler(WATER, this.tank);
    this.crafting.setInventorySlotContents(INPUT_SIZE, new ItemStack(GradientItems.MIXING_DISCRIMINATOR));
  }

  public boolean hasFluid() {
    return this.tank.getFluid() != null;
  }

  public boolean hasInput(final int slot) {
    return !this.getInput(slot).isEmpty();
  }

  public boolean hasOutput() {
    return !this.getOutput().isEmpty();
  }

  @Nullable
  public FluidStack getFluid() {
    return this.tank.getFluid();
  }

  public ItemStack getInput(final int slot) {
    return this.inventory.getStackInSlot(slot);
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(OUTPUT_SLOT);
  }

  public ItemStack takeInput(final int slot, final EntityPlayer player) {
    final ItemStack input = this.inventory.extractItem(slot, this.inventory.getSlotLimit(slot), false);
    this.updateRecipe(RecipeHelper.getPlayerAge(player));
    this.sync();
    return input;
  }

  public ItemStack takeOutput() {
    final ItemStack output = this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(OUTPUT_SLOT), false);
    this.sync();
    return output;
  }

  private int findOpenSlot() {
    for(int slot = 0; slot < INPUT_SIZE; slot++) {
      if(!this.hasInput(slot)) {
        return slot;
      }
    }

    return -1;
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    final int slot = this.findOpenSlot();

    // No space
    if(slot == -1) {
      return stack;
    }

    final ItemStack input = stack.splitStack(1);
    this.inventory.setStackInSlot(slot, input);

    this.updateRecipe(RecipeHelper.getPlayerAge(player));
    this.passes = 0;

    if(this.recipe != null) {
      this.ticks = this.recipe.ticks;
    }

    this.sync();

    return stack;
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

      this.tank.setFluid(null);

      for(int slot = 0; slot < INPUT_SIZE; slot++) {
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);
      }

      this.inventory.setStackInSlot(INPUT_SIZE, this.recipe.getCraftingResult(this.crafting));
      this.recipe = null;

      this.sync();
    }
  }

  public void mix() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks >= this.recipe.ticks) {
      ((WorldServer)this.world).spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.pos.getX() + 0.5d, this.pos.getY() + 0.5d, this.pos.getZ() + 0.5d, 10, 0.1d, 0.1d, 0.1d, 0.01d);

      this.ticks = 0;
      this.passes++;

      this.markDirty();
    }
  }

  private void updateRecipe(final Age age) {
    this.recipe = this.findRecipe(age);
  }

  @Nullable
  private MixingRecipe findRecipe(final Age age) {
    this.container.setPlayerAge(age);
    this.container.setFluid(this.tank.getFluid());

    for(int slot = 0; slot < INPUT_SIZE; slot++) {
      this.crafting.setInventorySlotContents(slot, this.getInput(slot));
    }

    final IRecipe recipe = CraftingUtil.findMatchingRecipe(this.crafting, this.world);

    if(!(recipe instanceof MixingRecipe)) {
      return null;
    }

    return (MixingRecipe)recipe;
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
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
    this.tank.readFromNBT(compound.getCompoundTag("tank"));
    this.passes = compound.getInteger("passes");
    this.ticks = compound.getInteger("ticks");
    this.container.setPlayerAge(Age.get(compound.getInteger("player_age")));

    this.updateRecipe(this.container.getPlayerAge());

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
      return FLUID_HANDLER_CAPABILITY.cast(this.tanks);
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
