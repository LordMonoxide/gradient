package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.MixingRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

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
      TileMixingBasin.this.updateRecipe();
      TileMixingBasin.this.sync();
      return filled;
    }

    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
      final FluidStack drained = super.drain(resource, doDrain);
      TileMixingBasin.this.updateRecipe();
      TileMixingBasin.this.sync();
      return drained;
    }

    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
      final FluidStack drained = super.drain(maxDrain, doDrain);
      TileMixingBasin.this.updateRecipe();
      TileMixingBasin.this.sync();
      return drained;
    }
  };

  public static final int INPUT_SIZE = 5;
  private static final int OUTPUT_SLOT = INPUT_SIZE;

  private final ItemStackHandler inventory = new ItemStackHandler(INPUT_SIZE + 1) {
    @Override
    public int getSlotLimit(final int slot) {
      if(slot < INPUT_SIZE) {
        return 1;
      }

      return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
      if(slot == OUTPUT_SLOT) {
        return false;
      }

      return super.isItemValid(slot, stack);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if(!this.isItemValid(slot, stack) && !TileMixingBasin.this.forceInsert) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
      if(slot < INPUT_SIZE) {
        final ItemStack stack = this.getStackInSlot(slot);

        if(!stack.isEmpty()) {
          if(TileMixingBasin.this.recipe == null) {
            TileMixingBasin.this.passes = 0;
            TileMixingBasin.this.ticks = 0;
            TileMixingBasin.this.updateRecipe();

            if(TileMixingBasin.this.recipe != null) {
              TileMixingBasin.this.ticks = TileMixingBasin.this.recipe.ticks;
            }
          }
        } else {
          TileMixingBasin.this.recipe = null;
          TileMixingBasin.this.passes = 0;
          TileMixingBasin.this.ticks = 0;
        }
      }

      TileMixingBasin.this.sync();
    }
  };

  @Nullable
  private MixingRecipe recipe;
  private Age age = Age.AGE1;
  private int passes;
  private int ticks;
  private boolean forceInsert;

  public TileMixingBasin() {
    this.tanks.addHandler(WATER, this.tank);
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
    this.age = AgeUtils.getPlayerAge(player);
    return this.inventory.extractItem(slot, this.inventory.getSlotLimit(slot), false);
  }

  public ItemStack takeOutput() {
    return this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(OUTPUT_SLOT), false);
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

    this.age = AgeUtils.getPlayerAge(player);
    this.inventory.setStackInSlot(slot, stack.splitStack(1));

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
      final Random rand = this.getWorld().rand;

      if(rand.nextInt(2) == 0) {
        final double radius = rand.nextDouble() * 0.2d;
        final double angle = rand.nextDouble() * Math.PI * 2;

        final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.WATER_BUBBLE, x, this.pos.getY() + 0.4d, z, 1, 0.0d, 0.0d, 0.0d, 0.0d);
      }

      this.ticks++;
      this.markDirty();
    }

    if(this.ticks >= this.recipe.ticks && this.passes >= this.recipe.passes) {
      this.passes = 0;
      this.tank.setFluid(null);

      final ItemStack output = this.recipe.getRecipeOutput().copy();

      for(int slot = 0; slot < INPUT_SIZE; slot++) {
        this.inventory.setStackInSlot(slot, ItemStack.EMPTY);
      }

      this.forceInsert = true;
      this.inventory.setStackInSlot(OUTPUT_SLOT, output);
      this.forceInsert = false;
    }
  }

  public void mix() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks >= this.recipe.ticks) {
      this.getWorld().playSound(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.BLOCKS, 0.8f + this.getWorld().rand.nextFloat(), this.getWorld().rand.nextFloat() * 0.7f + 0.3f, false);

      this.ticks = 0;
      this.passes++;

      this.markDirty();
    }
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(MixingRecipe.class, recipe -> recipe.matches(this.inventory, this.age, this.tank.getFluid(), 0, INPUT_SIZE - 1));
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
    compound.setInteger("passes", this.passes);
    compound.setInteger("ticks", this.ticks);
    compound.setInteger("player_age", this.age.value());

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
    this.age = Age.get(compound.getInteger("player_age"));

    this.updateRecipe();

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
    final IBlockState oldState = this.world.getBlockState(this.pos);
    this.readFromNBT(pkt.getNbtCompound());
    this.world.notifyBlockUpdate(this.pos, oldState, this.world.getBlockState(this.pos), 2);
  }
}
