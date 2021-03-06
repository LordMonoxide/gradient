package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileClayOven extends HeatSinker {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  public static final int TOTAL_SLOTS_COUNT = 2;
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT) {
    @Override
    public int getSlotLimit(final int slot) {
      if(slot == OUTPUT_SLOT) {
        return super.getSlotLimit(slot);
      }

      return 1;
    }

    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
      if(slot == INPUT_SLOT) {
        return
          !TileClayOven.this.hasInput() &&
          RecipeUtils.findRecipe(FirePitRecipe.class, recipe -> recipe.matches(stack)) != null;
      }

      return false;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if(!this.isItemValid(slot, stack) && !TileClayOven.this.forceInsert) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
      if(slot == INPUT_SLOT && TileClayOven.this.isCooking()) {
        return ItemStack.EMPTY;
      }

      return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
      final ItemStack stack = this.getStackInSlot(slot);

      if(slot == INPUT_SLOT) {
        if(!stack.isEmpty()) {
          TileClayOven.this.updateRecipe();
        } else {
          TileClayOven.this.recipe = null;
        }

        TileClayOven.this.ticks = 0;
      }

      TileClayOven.this.sync();
    }
  };

  @Nullable
  private FirePitRecipe recipe;
  private Age age = Age.AGE1;
  private int ticks;
  private boolean forceInsert;

  public boolean isCooking() {
    return this.recipe != null;
  }

  public float getCookingPercent() {
    if(!this.isCooking()) {
      return 0.0f;
    }

    return this.ticks / (this.recipe.ticks * this.getHeatScale());
  }

  public boolean hasInput() {
    return !this.getInput().isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }


  public ItemStack takeInput() {
    return this.inventory.extractItem(INPUT_SLOT, this.inventory.getSlotLimit(INPUT_SLOT), false);
  }

  public boolean hasOutput() {
    return !this.getOutput().isEmpty();
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(OUTPUT_SLOT);
  }

  public ItemStack takeOutput() {
    return this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(OUTPUT_SLOT), false);
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasInput()) {
      this.age = AgeUtils.getPlayerAge(player);
      this.inventory.setStackInSlot(INPUT_SLOT, stack.splitStack(1));

      return stack;
    }

    return stack;
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(FirePitRecipe.class, recipe -> recipe.matches(this.getInput(), this.age));
  }

  @Override
  protected void tickBeforeCooldown(final float tickScale) {

  }

  @Override
  protected void tickAfterCooldown(final float tickScale) {
    this.cook();

    if(this.getWorld().isRemote) {
      this.generateParticles();
    }
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return (float)Math.max(0.5d, Math.pow(this.getHeat() / 800, 2));
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private float getHeatScale() {
    if(this.recipe == null) {
      return 1.0f;
    }

    return 1.0f - ((this.getHeat() - this.recipe.temperature) / 2000.0f + 0.1f);
  }

  private void cook() {
    if(this.recipe == null) {
      return;
    }

    final float heatScale = this.getHeatScale();

    if(this.ticks < this.recipe.ticks * heatScale) {
      if(this.getHeat() >= this.recipe.temperature) {
        this.ticks++;
        this.markDirty();
      }
    }

    if(this.ticks >= this.recipe.ticks * heatScale) {
      final ItemStack output = this.recipe.getRecipeOutput().copy();
      this.inventory.extractItem(INPUT_SLOT, 1, false);
      this.forceInsert = true;
      this.inventory.insertItem(OUTPUT_SLOT, output, false);
      this.forceInsert = false;
    }
  }

  private void generateParticles() {
    if(this.hasHeat()) {
      final Random rand = this.getWorld().rand;

      if(rand.nextInt(10) == 0) {
        if(this.recipe != null && this.getHeat() >= this.recipe.temperature) {
          final double radius = rand.nextDouble() * 0.15d;
          final double angle  = rand.nextDouble() * Math.PI * 2;

          final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
          final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

          this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
        }
      }
    }
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
}
