package lordmonoxide.gradient.blocks.firepit;

import buildcraft.lib.misc.CraftingUtil;
import lordmonoxide.gradient.GradientFuel;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import lordmonoxide.gradient.blocks.heat.HeatProducer;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// With a single furnace, a crucible will heat up to 1038 degrees
// Two furnaces, 1152
// Two layers of furnaces, 1693
// Three layers of furnaces, 1725

public class TileFirePit extends HeatProducer {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @GameRegistry.ObjectHolder("gradient:firepit_discriminator")
  private static final Item FIREPIT_DISCRIMINATOR = null;

  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + 2;

  public static final int FIRST_FUEL_SLOT = 0;
  public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
  public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + 1;

  private final ContainerFirePit container = new ContainerFirePit();
  private final InventoryCrafting crafting = new InventoryCrafting(this.container, 2, 1);
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);

  @Nullable
  private FirePitRecipe recipe;
  private int ticks;

  private final GradientFuel.BurningFuel[] fuels = new GradientFuel.BurningFuel[FUEL_SLOTS_COUNT];

  private final Map<BlockPos, Hardening> hardenables = new HashMap<>();

  private boolean firstTick = true;

  private int lastLight;

  public TileFirePit() {
    this.crafting.setInventorySlotContents(1, new ItemStack(FIREPIT_DISCRIMINATOR));
  }

  public boolean hasFurnace(final IBlockState state) {
    return state.getValue(BlockFirePit.HAS_FURNACE);
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
    return this.fuels[slot] != null;
  }

  public GradientFuel.BurningFuel getBurningFuel(final int slot) {
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

  public ItemStack insertItem(final ItemStack stack) {
    if(GradientFuel.has(stack)) {
      for(int slot = 0; slot < FUEL_SLOTS_COUNT; slot++) {
        if(!this.hasFuel(slot)) {
          final ItemStack input = stack.splitStack(1);
          this.setFuelSlot(slot, input);
          this.sync();
          return stack;
        }
      }
    }

    if(!this.hasInput()) {
      this.recipe = this.findRecipe(stack);

      if(this.recipe == null) {
        return stack;
      }

      this.ticks = 0;

      final ItemStack input = stack.splitStack(1);
      this.inventory.setStackInSlot(FIRST_INPUT_SLOT, input);
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

  public void updateHardenable(final BlockPos pos) {
    if(this.world.isRemote) {
      return;
    }

    final Block block = this.getWorld().getBlockState(pos).getBlock();

    if(!(block instanceof Hardenable)) {
      this.hardenables.remove(pos);
      return;
    }

    this.hardenables.put(pos, new Hardening((Hardenable)block, pos));
  }

  private void findSurroundingHardenables() {
    final BlockPos north = this.pos.north();
    final BlockPos south = this.pos.south();

    this.updateHardenable(north.east());
    this.updateHardenable(north);
    this.updateHardenable(north.west());
    this.updateHardenable(this.pos.east());
    this.updateHardenable(this.pos.west());
    this.updateHardenable(south.east());
    this.updateHardenable(south);
    this.updateHardenable(south.west());
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
      if(this.firstTick) {
        this.findSurroundingHardenables();
        this.firstTick = false;
      }

      this.hardenHardenables();
    }
  }

  private void igniteFuel() {
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(!this.isBurning(i) && !this.getFuelSlot(i).isEmpty()) {
        final GradientFuel.Fuel fuel = GradientFuel.get(this.getFuelSlot(i));

        if(this.canIgnite(fuel)) {
          this.fuels[i] = new GradientFuel.BurningFuel(fuel);
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
      this.inventory.insertItem(FIRST_OUTPUT_SLOT, this.recipe.getCraftingResult(this.crafting), false);
      this.recipe = null;
      this.sync();
    }
  }

  private void hardenHardenables() {
    if(this.hardenables.isEmpty()) {
      return;
    }

    this.hardenables.keySet().removeIf(pos -> !(this.getWorld().getBlockState(pos).getBlock() instanceof Hardenable) || ((Hardenable)this.getWorld().getBlockState(pos).getBlock()).isHardened(this.getWorld().getBlockState(pos)));
    this.hardenables.values().stream()
      .map(Hardening::tick)
      .filter(Hardening::isHardened)
      .collect(Collectors.toList()) // Gotta decouple here to avoid concurrent modification exceptions
      .forEach(hardenable -> this.getWorld().setBlockState(hardenable.pos, hardenable.block.getHardened(this.getWorld().getBlockState(hardenable.pos))));
  }

  private void updateLight() {
    final int light = this.getLightLevel(this.getBlockState());

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

      // Smoke
      final double radius = this.getWorld().rand.nextDouble() * 0.35d;
      final double angle  = this.getWorld().rand.nextDouble() * Math.PI * 2;

      final double x = this.pos.getX() + 0.5d + radius * Math.cos(angle);
      final double z = this.pos.getZ() + 0.5d + radius * Math.sin(angle);

      this.getWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, this.pos.getY() + 0.1d, z, 0.0d, 0.0d, 0.0d);
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
        final GradientFuel.BurningFuel fuel = this.fuels[slot];

        fuel.tick();

        if(fuel.isDepleted()) {
          this.fuels[slot] = null;
          this.setFuelSlot(slot, ItemStack.EMPTY);
        }

        if(fuel.fuel.burnTemp > this.getHeat()) {
          temperatureChange += fuel.fuel.heatPerSec;
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

  private ItemStack getFuelSlot(final int slot) {
    return this.inventory.getStackInSlot(FIRST_FUEL_SLOT + slot);
  }

  private void setFuelSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_FUEL_SLOT + slot, stack);
  }

  private boolean canIgnite(final GradientFuel.Fuel fuel) {
    return this.getHeat() >= fuel.ignitionTemp;
  }

  @Nullable
  private FirePitRecipe findRecipe(final ItemStack input) {
    this.crafting.setInventorySlotContents(0, input);
    final IRecipe recipe = CraftingUtil.findMatchingRecipe(this.crafting, this.world);

    if(!(recipe instanceof FirePitRecipe)) {
      return null;
    }

    return (FirePitRecipe)recipe;
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());

    final NBTTagList fuels = new NBTTagList();

    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        final GradientFuel.BurningFuel fuel = this.getBurningFuel(i);

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        fuel.writeToNbt(tag);
        fuels.appendTag(tag);
      }
    }

    compound.setTag("fuel", fuels);

    compound.setInteger("ticks", this.ticks);

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
        this.fuels[slot] = GradientFuel.BurningFuel.fromNbt(GradientFuel.get(this.getFuelSlot(slot)), tag);
      }
    }

    this.ticks = compound.getInteger("ticks");
    this.recipe = this.findRecipe(this.getInput());

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

  public final class Hardening {
    public final Hardenable block;
    public final BlockPos pos;
    private final int hardenTicksTotal;
    private int hardenTicks;

    private Hardening(final Hardenable block, final BlockPos pos) {
      this(block, pos, block.getHardeningTime(TileFirePit.this.world.getBlockState(pos)) * 20);
    }

    private Hardening(final Hardenable block, final BlockPos pos, final int hardenTicksTotal) {
      this.block = block;
      this.pos = pos;
      this.hardenTicksTotal = hardenTicksTotal;
    }

    public Hardening tick() {
      this.hardenTicks++;
      return this;
    }

    public boolean isHardened() {
      return this.hardenTicks >= this.hardenTicksTotal;
    }
  }
}
