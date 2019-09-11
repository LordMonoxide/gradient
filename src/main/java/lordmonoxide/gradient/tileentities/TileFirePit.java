package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientFluids;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockFirePit;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.HeatProducer;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.recipes.FuelRecipe;
import lordmonoxide.gradient.recipes.HardeningRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

// With a single furnace, a crucible will heat up to 1038 degrees
// Two furnaces, 1152
// Two layers of furnaces, 1693
// Three layers of furnaces, 1725

public class TileFirePit extends HeatProducer {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + 2;

  public static final int FIRST_FUEL_SLOT = 0;
  public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
  public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + 1;

  private static int FLUID_CAPACITY = 100;

  private final Fluid air = GradientFluids.AIR;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  public final FluidTank tank = new FluidTank(FLUID_CAPACITY) {
    @Override
    public boolean canFillFluidType(final FluidStack fluid) {
      return fluid.getFluid() == TileFirePit.this.air && super.canFillFluidType(fluid);
    }

    @Override
    protected void onContentsChanged() {
      TileFirePit.this.sync();
    }
  };

  @Nullable
  private FirePitRecipe recipe;
  private Age age = Age.AGE1;
  private int ticks;

  private final Fuel[] fuels = new Fuel[FUEL_SLOTS_COUNT];

  private final Map<BlockPos, Hardening> hardenables = new HashMap<>();

  private int lastLight;

  public TileFirePit() {
    this.tank.setCanDrain(false);
  }

  public boolean hasFurnace(final IBlockState state) {
    return state.getBlock() == GradientBlocks.FIRE_PIT && state.getValue(BlockFirePit.HAS_FURNACE);
  }

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

  public boolean hasFuel(final int slot) {
    return !this.getFuel(slot).isEmpty();
  }

  public ItemStack getFuel(final int slot) {
    return this.inventory.getStackInSlot(FIRST_FUEL_SLOT + slot);
  }

  public ItemStack takeFuel(final int slot) {
    final ItemStack fuel = this.inventory.extractItem(FIRST_FUEL_SLOT + slot, this.inventory.getSlotLimit(FIRST_FUEL_SLOT + slot), false);
    this.fuels[slot] = null;
    this.sync();
    return fuel;
  }

  public boolean hasInput() {
    return !this.getInput().isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(FIRST_INPUT_SLOT);
  }

  public ItemStack takeInput() {
    final ItemStack input = this.inventory.extractItem(FIRST_INPUT_SLOT, this.inventory.getSlotLimit(FIRST_INPUT_SLOT), false);
    this.recipe = null;
    this.ticks = 0;
    this.sync();
    return input;
  }

  public boolean hasOutput() {
    return !this.getOutput().isEmpty();
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(FIRST_OUTPUT_SLOT);
  }

  public ItemStack takeOutput() {
    final ItemStack output = this.inventory.extractItem(FIRST_OUTPUT_SLOT, this.inventory.getSlotLimit(FIRST_OUTPUT_SLOT), false);
    this.sync();
    return output;
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player, final IBlockState state) {
    final Age age = AgeUtils.getPlayerAge(player);

    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(!this.hasFuel(slot)) {
        final FuelRecipe recipe = RecipeUtils.findRecipe(FuelRecipe.class, r -> r.matches(stack));

        if(recipe != null) {
          final ItemStack input = stack.splitStack(1);
          this.setFuelSlot(slot, input);
          this.fuels[slot] = new Fuel(recipe, age);
          this.sync();
          return stack;
        }
      }
    }

    if(!this.hasInput() && !this.hasFurnace(state)) {
      this.age = age;

      final ItemStack input = stack.splitStack(1);
      this.inventory.setStackInSlot(FIRST_INPUT_SLOT, input);

      this.updateRecipe();
      this.ticks = 0;

      this.sync();
      return stack;
    }

    return stack;
  }

  public int getLightLevel(final IBlockState state) {
    if(this.getHeat() == 0) {
      return 0;
    }

    return Math.min(
      !this.hasFurnace(state) ?
        (int)(this.getHeat() / 800 * 11) + 4 :
        (int)(this.getHeat() / 1000 * 9) + 2,
      15
    );
  }

  public void light() {
    if(this.isBurning()) {
      return;
    }

    this.setHeat(Math.max(50, this.getHeat()));
    this.sync();
  }

  public void attachFurnace() {
    this.sync();
  }

  public void updateHardenable(final BlockPos pos, final Age age) {
    if(this.world.isRemote) {
      return;
    }

    final IBlockState state = this.getWorld().getBlockState(pos);

    final HardeningRecipe recipe = RecipeUtils.findRecipe(HardeningRecipe.class, r -> r.matches(state, age));

    if(recipe == null) {
      this.hardenables.remove(pos);
      return;
    }

    this.hardenables.put(pos, new Hardening(recipe, age));
  }

  public void updateSurroundingHardenables(final Age age) {
    final BlockPos north = this.pos.north();
    final BlockPos south = this.pos.south();

    this.updateHardenable(north.east(), age);
    this.updateHardenable(north, age);
    this.updateHardenable(north.west(), age);
    this.updateHardenable(this.pos.east(), age);
    this.updateHardenable(this.pos.west(), age);
    this.updateHardenable(south.east(), age);
    this.updateHardenable(south, age);
    this.updateHardenable(south.west(), age);
  }

  @Override
  protected void tickBeforeCooldown() {
    this.igniteFuel();
  }

  @Override
  protected void tickAfterCooldown() {
    super.tickAfterCooldown();

    this.cook();
    this.updateLight();

    if(this.getWorld().isRemote) {
      this.generateParticles();
      this.playSounds();
    } else {
      this.hardenHardenables();
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

  private void cook() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks < this.recipe.ticks) {
      if(this.getHeat() >= this.recipe.temperature) {
        this.ticks++;
        this.markDirty();
      }
    }

    if(this.ticks >= this.recipe.ticks) {
      this.inventory.extractItem(FIRST_INPUT_SLOT, 1, false);
      this.inventory.insertItem(FIRST_OUTPUT_SLOT, this.recipe.getRecipeOutput().copy(), false);
      this.recipe = null;
      this.sync();
    }
  }

  private void hardenHardenables() {
    if(this.hardenables.isEmpty()) {
      return;
    }

    final Iterator<Map.Entry<BlockPos, Hardening>> it = this.hardenables.entrySet().iterator();

    final Map<BlockPos, IBlockState> toAdd = new HashMap<>();

    while(it.hasNext()) {
      final Map.Entry<BlockPos, Hardening> entry = it.next();
      final BlockPos pos = entry.getKey();
      final Hardening hardening = entry.getValue();
      final IBlockState current = this.getWorld().getBlockState(pos);

      if(!hardening.recipe.matches(current, hardening.age)) {
        it.remove();
        continue;
      }

      hardening.tick();

      if(hardening.isHardened()) {
        toAdd.put(pos, hardening.recipe.getCraftingResult(current));
        it.remove();
      }
    }

    this.markDirty();

    for(final Map.Entry<BlockPos, IBlockState> entry : toAdd.entrySet()) {
      this.getWorld().setBlockState(entry.getKey(), entry.getValue());
    }
  }

  private void updateLight() {
    final int light = this.getLightLevel(this.getBlockState());

    if(this.lastLight != light) {
      this.getWorld().markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.getWorld().checkLight(this.pos);

      this.lastLight = light;
    }
  }

  private float getHeatRatio() {
    return (float)this.tank.getFluidAmount() / this.tank.getCapacity();
  }

  private void generateParticles() {
    if(this.hasHeat()) {
      final Random rand = this.getWorld().rand;

      final int amount = 1 + (int)(this.getHeatRatio() / 10.0f);

      for(int i = 0; i < amount; i++) {
        // Fire
        if(this.isBurning()) {
          if(this.getHeat() >= 200 || rand.nextInt(600) >= 400 - this.getHeat() * 2) {
            final double radius = rand.nextDouble() * 0.25d;
            final double angle = rand.nextDouble() * Math.PI * 2;

            final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
            final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

            this.getWorld().spawnParticle(EnumParticleTypes.FLAME, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
          }

          if(this.tank.getFluidAmount() > 0) {
            //TODO uhh empty if statement??
          }
        }

        // Smoke
        if(this.getHeat() >= 200 || !this.isBurning()) {
          final double radius = rand.nextDouble() * 0.35d;
          final double angle = rand.nextDouble() * Math.PI * 2;

          final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
          final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

          this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
        }
      }
    }
  }

  private void playSounds() {
    if(this.getHeat() > 0) {
      if(this.getWorld().rand.nextInt(40 - (int)(this.getHeatRatio() / 8.0f)) == 0) {
        this.getWorld().playSound(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.8f + this.getWorld().rand.nextFloat(), this.getWorld().rand.nextFloat() * 0.7f + 0.3f, false);
      }
    }
  }

  @Override
  protected float calculateHeatGain() {
    float temperatureChange = 0;

    final float airBonus = 1.0f + this.getHeatRatio() / 3.0f;
    this.tank.drainInternal(2, true);

    for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
      if(this.isBurning(slot)) {
        final Fuel fuel = this.fuels[slot];

        fuel.tick();

        if(fuel.isDepleted()) {
          this.fuels[slot] = null;
          this.setFuelSlot(slot, ItemStack.EMPTY);
        }

        if(fuel.recipe.burnTemp > this.getHeat()) {
          temperatureChange += fuel.recipe.heatPerSec * airBonus;
        }
      }
    }

    return temperatureChange;
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return !this.hasFurnace(state) ?
      (float)Math.pow(this.getHeat() / 500 + 1, 2) / 1.5f :
      (float)Math.pow(this.getHeat() / 1600 + 1, 2);
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private void setFuelSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }

  private boolean canIgnite(final Fuel fuel) {
    return this.getHeat() >= fuel.recipe.ignitionTemp;
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(FirePitRecipe.class, recipe -> recipe.matches(this.inventory, this.age, FIRST_INPUT_SLOT, FIRST_INPUT_SLOT));
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    this.tank.writeToNBT(compound);

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

    compound.setInteger("player_age", this.age.value());
    compound.setInteger("ticks", this.ticks);

    final NBTTagList hardenings = new NBTTagList();
    for(final Map.Entry<BlockPos, Hardening> entry : this.hardenables.entrySet()) {
      final NBTTagCompound hardening = new NBTTagCompound();
      hardening.setTag("pos", NBTUtil.createPosTag(entry.getKey()));
      hardening.setString("recipe", entry.getValue().recipe.getRegistryName().toString());
      hardening.setInteger("age", entry.getValue().age.value());
      hardening.setInteger("ticks", entry.getValue().hardenTicks);

      hardenings.appendTag(hardening);
    }

    compound.setTag("hardening", hardenings);

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.lastLight = -1;

    Arrays.fill(this.fuels, null);

    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    this.tank.readFromNBT(compound);

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

    final int age = compound.getInteger("player_age");

    try {
      this.age = Age.get(age);
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.warn("Invalid age in {}: {}", this, age);
    }

    this.ticks = compound.getInteger("ticks");

    this.hardenables.clear();

    for(final NBTBase tag : compound.getTagList("hardening", Constants.NBT.TAG_COMPOUND)) {
      final NBTTagCompound hardeningNbt = (NBTTagCompound)tag;

      final BlockPos pos = NBTUtil.getPosFromTag(hardeningNbt.getCompoundTag("pos"));
      final HardeningRecipe recipe = (HardeningRecipe)ForgeRegistries.RECIPES.getValue(new ResourceLocation(hardeningNbt.getString("recipe")));
      final Age age1 = Age.get(hardeningNbt.getInteger("age"));
      final int ticks = hardeningNbt.getInteger("ticks");

      final Hardening hardening = new Hardening(recipe, age1);
      hardening.hardenTicks = ticks;

      this.hardenables.put(pos, hardening);
    }

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
      return FLUID_HANDLER_CAPABILITY.cast(this.tank);
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

    public float percentBurned() {
      return (float)this.burnTicks / this.burnTicksTotal;
    }
  }

  public static final class Hardening {
    public final HardeningRecipe recipe;
    public final Age age;
    private int hardenTicks;

    private Hardening(final HardeningRecipe recipe, final Age age) {
      this.recipe = recipe;
      this.age = age;
    }

    private void tick() {
      this.hardenTicks++;
    }

    public boolean isHardened() {
      return this.hardenTicks >= this.recipe.ticks;
    }
  }
}
