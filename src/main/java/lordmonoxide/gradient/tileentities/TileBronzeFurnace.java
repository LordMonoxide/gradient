package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.heat.HeatProducer;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.FuelRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileBronzeFurnace extends HeatProducer {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  public static final int FUEL_SLOTS_COUNT = 6;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT;

  public static final int FIRST_FUEL_SLOT = 0;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);

  private final Fuel[] fuels = new Fuel[FUEL_SLOTS_COUNT];

  private int lastLight;

  public boolean isBurning() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        return true;
      }
    }

    return false;
  }

  public boolean isBurning(final int slot) {
    return this.fuels[slot] != null && this.fuels[slot].isBurning;
  }

  public Fuel getBurningFuel(final int slot) {
    return this.fuels[slot];
  }

  public int getLightLevel() {
    if(this.getHeat() == 0) {
      return 0;
    }

    return Math.min((int)(this.getHeat() / 1000 * 9) + 2, 15);
  }

  public void light() {
    if(this.isBurning()) {
      return;
    }

    this.setHeat(Math.max(50, this.getHeat()));
    this.sync();
  }

  @Override
  protected void tickBeforeCooldown() {
    this.igniteFuel();
  }

  @Override
  protected void tickAfterCooldown() {
    super.tickAfterCooldown();

    this.updateLight();

    if(this.getWorld().isRemote) {
      this.generateParticles();
      this.playSounds();
    }
  }

  private void igniteFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(!this.isBurning(i) && this.fuels[i] != null) {
        if(this.canIgnite(this.fuels[i])) {
          this.fuels[i].ignite();
          this.sync();
        }
      }
    }
  }

  private void updateLight() {
    final int light = this.getLightLevel();

    if(this.lastLight != light) {
      this.getWorld().markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.getWorld().checkLight(this.pos);

      this.lastLight = light;
    }
  }

  private void generateParticles() {
    if(this.hasHeat()) {
      if(this.isBurning()) { // Fire
        final double radius = this.getWorld().rand.nextDouble() * 0.25d;
        final double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;

        final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

        this.getWorld().spawnParticle(EnumParticleTypes.FLAME, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
      }

      { // Smoke
        final double radius = this.getWorld().rand.nextDouble() * 0.35d;
        final double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;

        final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
        final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

        this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
      }
    }
  }

  private void playSounds() {
    if(this.getHeat() > 0) {
      if(this.getWorld().rand.nextInt(40) == 0) {
        this.getWorld().playSound(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.8f + this.getWorld().rand.nextFloat(), this.getWorld().rand.nextFloat() * 0.7f + 0.3f, false);
      }
    }
  }

  @Override
  protected float calculateHeatGain() {
    float temperatureChange = 0;

    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(this.isBurning(slot)) {
        final Fuel fuel = this.fuels[slot];

        fuel.tick();

        if(fuel.isDepleted()) {
          this.fuels[slot] = null;
          this.setFuelSlot(slot, ItemStack.EMPTY);
        }

        if(fuel.recipe.burnTemp > this.getHeat()) {
          temperatureChange += fuel.recipe.heatPerSec;
        }
      }
    }

    return temperatureChange;
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return (float)Math.pow(this.getHeat() / 1600 + 1, 2);
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private ItemStack getFuelSlot(final int slot) {
    return this.inventory.getStackInSlot(FIRST_FUEL_SLOT + slot);
  }

  private void setFuelSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }

  private boolean canIgnite(final Fuel fuel) {
    return this.getHeat() >= fuel.recipe.ignitionTemp;
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());

    final NBTTagList fuels = new NBTTagList();

    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        final Fuel fuel = this.getBurningFuel(i);

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setString("recipe", fuel.recipe.getRegistryName().toString());
        tag.setInteger("age", fuel.age.value());
        tag.setInteger("ticks", fuel.burnTicks);
        tag.setBoolean("burning", fuel.isBurning);
        fuels.appendTag(tag);
      }
    }

    compound.setTag("fuel", fuels);

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.lastLight = -1;

    Arrays.fill(this.fuels, null);

    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    final NBTTagList fuels = compound.getTagList("fuel", Constants.NBT.TAG_COMPOUND);

    for(int i = 0; i < fuels.tagCount(); i++) {
      final NBTTagCompound tag = fuels.getCompoundTagAt(i);

      final int slot = tag.getInteger("slot");

      if(slot < FUEL_SLOTS_COUNT) {
        final FuelRecipe recipe = (FuelRecipe)ForgeRegistries.RECIPES.getValue(new ResourceLocation(tag.getString("recipe")));
        final Age age1 = Age.get(tag.getInteger("age"));
        final int ticks = tag.getInteger("ticks");
        final boolean burning = tag.getBoolean("burning");

        final Fuel fuel = new Fuel(recipe, age1);
        fuel.burnTicks = ticks;
        fuel.isBurning = burning;

        this.fuels[slot] = fuel;
      }
    }

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

  public static final class Fuel {
    public final FuelRecipe recipe;
    public final Age age;
    private final int burnTicksTotal;
    private int burnTicks;
    private boolean isBurning;

    private Fuel(final FuelRecipe recipe, final Age age) {
      this.recipe = recipe;
      this.age = age;
      this.burnTicksTotal = this.recipe.duration * 20;
    }

    private void tick() {
      this.burnTicks++;
    }

    private void ignite() {
      this.isBurning = true;
    }

    public boolean isDepleted() {
      return this.burnTicks >= this.burnTicksTotal;
    }

    public float burnPercent() {
      return (float)this.burnTicks / this.burnTicksTotal;
    }
  }
}