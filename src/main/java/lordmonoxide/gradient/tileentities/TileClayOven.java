package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.recipes.GradientRecipeTypes;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class TileClayOven extends HeatSinker {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  public static final int TOTAL_SLOTS_COUNT = 2;
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);

  @Nullable
  private FirePitRecipe recipe;
  private Age age = Age.AGE1;
  private int ticks;

  public TileClayOven() {
    super(GradientTileEntities.CLAY_OVEN_HARDENED);
  }

  public boolean hasInput() {
    return !this.getInput().isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }

  public ItemStack takeInput() {
    final ItemStack input = this.inventory.extractItem(INPUT_SLOT, this.inventory.getSlotLimit(INPUT_SLOT), false);
    this.recipe = null;
    this.ticks = 0;
    this.sync();
    return input;
  }

  public boolean hasOutput() {
    return !this.getOutput().isEmpty();
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(OUTPUT_SLOT);
  }

  public ItemStack takeOutput() {
    final ItemStack output = this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(OUTPUT_SLOT), false);
    this.sync();
    return output;
  }

  public ItemStack insertItem(final ItemStack stack, final PlayerEntity player) {
    if(!this.hasInput()) {
      this.age = AgeUtils.getPlayerAge(player);

      final ItemStack input = stack.split(1);
      this.inventory.setStackInSlot(INPUT_SLOT, input);

      this.updateRecipe();
      this.ticks = 0;

      this.sync();
      return stack;
    }

    return stack;
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(GradientRecipeTypes.FIREPIT, recipe -> recipe.matches(this.inventory, this.age, INPUT_SLOT, INPUT_SLOT));
  }

  @Override
  protected void tickBeforeCooldown() {

  }

  @Override
  protected void tickAfterCooldown() {
    this.cook();

    if(this.world.isRemote) {
      this.generateParticles();
    }
  }

  @Override
  protected float calculateHeatLoss(final BlockState state) {
    return (float)Math.max(0.5d, Math.pow(this.getHeat() / 800, 2));
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private void cook() {
    if(this.recipe == null) {
      return;
    }

    final float heatScale = 1.0f - ((this.getHeat() - this.recipe.temperature) / 2000.0f + 0.1f);

    if(this.ticks < this.recipe.ticks * heatScale) {
      if(this.getHeat() >= this.recipe.temperature) {
        this.ticks++;
        this.markDirty();
      }
    }

    if(this.ticks >= this.recipe.ticks * heatScale) {
      this.inventory.extractItem(INPUT_SLOT, 1, false);
      this.inventory.insertItem(OUTPUT_SLOT, this.recipe.getRecipeOutput().copy(), false);
      this.recipe = null;
      this.sync();
    }
  }

  private void generateParticles() {
    if(this.hasHeat()) {
      final Random rand = this.world.rand;

      if(rand.nextInt(10) == 0) {
        if(this.recipe != null && this.getHeat() >= this.recipe.temperature) {
          final double radius = rand.nextDouble() * 0.15d;
          final double angle  = rand.nextDouble() * Math.PI * 2;

          final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
          final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

          this.world.addParticle(ParticleTypes.SMOKE, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
        }
      }
    }
  }

  @Override
  public CompoundNBT write(final CompoundNBT compound) {
    compound.put("inventory", this.inventory.serializeNBT());

    compound.putInt("player_age", this.age.value());
    compound.putInt("ticks", this.ticks);

    return super.write(compound);
  }

  @Override
  public void read(final CompoundNBT compound) {
    final CompoundNBT inv = compound.getCompound("inventory");
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
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    return super.getCapability(capability, facing);
  }
}
