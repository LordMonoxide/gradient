package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientFood;
import lordmonoxide.gradient.GradientFuel;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import lordmonoxide.gradient.blocks.heat.HeatProducer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
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
  public static final int FUEL_SLOTS_COUNT = 3;
  public static final int INPUT_SLOTS_COUNT = 1;
  public static final int OUTPUT_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;
  
  public static final int FIRST_FUEL_SLOT = 0;
  public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
  public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  
  private final GradientFuel.BurningFuel[] fuels = new GradientFuel.BurningFuel[FUEL_SLOTS_COUNT];
  private final GradientFood.CookingFood[] foods = new GradientFood.CookingFood[INPUT_SLOTS_COUNT];
  
  private final Map<BlockPos, Hardening> hardenables = new HashMap<>();
  
  private boolean firstTick = true;
  
  private int lastLight;
  
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
  
  public boolean isCooking() {
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(this.isCooking(i)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isCooking(final int slot) {
    return this.foods[slot] != null;
  }
  
  public GradientFood.CookingFood getCookingFood(final int slot) {
    return this.foods[slot];
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
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(!this.isCooking(i) && !this.getFoodSlot(i).isEmpty()) {
        final GradientFood.Food food = GradientFood.get(this.getFoodSlot(i));
        
        if(this.canCook(food)) {
          this.foods[i] = new GradientFood.CookingFood(food);
        }
      }
      
      if(this.isCooking(i)) {
        final GradientFood.CookingFood food = this.getCookingFood(i);
        
        if(food.isCooked()) {
          this.foods[i] = null;
          this.setFoodSlot(i, ItemStack.EMPTY);
          this.setCookedSlot(i, food.food.cooked.copy());
        }
      }
    }
  }
  
  private void hardenHardenables() {
    if(this.hardenables.isEmpty()) {
      return;
    }
    
    this.hardenables.keySet().removeIf(pos -> !(this.getWorld().getBlockState(pos).getBlock() instanceof Hardenable));
    this.hardenables.values().stream()
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
        final GradientFuel.BurningFuel fuel = this.fuels[slot];
        
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
      (float)Math.pow((this.getHeat() / 500) + 1, 2) / 1.5f :
      (float)Math.pow((this.getHeat() / 1600) + 1, 2);
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
  
  private ItemStack getFoodSlot(final int slot) {
    return this.inventory.getStackInSlot(FIRST_INPUT_SLOT + slot);
  }
  
  private void setFoodSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_INPUT_SLOT + slot, stack);
  }
  
  private boolean canCook(final GradientFood.Food food) {
    return this.getHeat() >= food.cookTemp;
  }
  
  private void setCookedSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_OUTPUT_SLOT + slot, stack);
  }
  
  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
  
    final NBTTagList fuels = new NBTTagList();
    final NBTTagList foods = new NBTTagList();
    
    for(int i = 0; i < FUEL_SLOTS_COUNT; i++) {
      if(this.isBurning(i)) {
        final GradientFuel.BurningFuel fuel = this.getBurningFuel(i);
        
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", fuel.burnStart - System.currentTimeMillis());
        tag.setLong("until", fuel.burnUntil - System.currentTimeMillis());
        fuels.appendTag(tag);
      }
    }
    
    for(int i = 0; i < INPUT_SLOTS_COUNT; i++) {
      if(this.isCooking(i)) {
        final GradientFood.CookingFood food = this.getCookingFood(i);
        
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", food.cookStart - System.currentTimeMillis());
        tag.setLong("until", food.cookUntil - System.currentTimeMillis());
        foods.appendTag(tag);
      }
    }
    
    compound.setTag("fuel", fuels);
    compound.setTag("food", foods);
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.lastLight = -1;
    
    Arrays.fill(this.fuels, null);
    Arrays.fill(this.foods, null);
    
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    
    final NBTTagList fuels = compound.getTagList("fuel", Constants.NBT.TAG_COMPOUND);
    final NBTTagList foods = compound.getTagList("food", Constants.NBT.TAG_COMPOUND);
    
    for(int i = 0; i < fuels.tagCount(); i++) {
      final NBTTagCompound tag = fuels.getCompoundTagAt(i);
      
      final int slot = tag.getInteger("slot");
      
      if(slot < FUEL_SLOTS_COUNT) {
        this.fuels[slot] = new GradientFuel.BurningFuel(
          GradientFuel.get(this.getFuelSlot(slot)),
          tag.getLong("start") + System.currentTimeMillis(),
          tag.getLong("until") + System.currentTimeMillis()
        );
      }
    }
    
    for(int i = 0; i < foods.tagCount(); i++) {
      final NBTTagCompound tag = foods.getCompoundTagAt(i);
      
      final int slot = tag.getInteger("slot");
      
      if(slot < INPUT_SLOTS_COUNT) {
        this.foods[slot] = new GradientFood.CookingFood(
          GradientFood.get(this.getFoodSlot(slot)),
          tag.getLong("start") + System.currentTimeMillis(),
          tag.getLong("until") + System.currentTimeMillis()
        );
      }
    }
    
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory : super.getCapability(capability, facing);
  }
  
  public static final class Hardening {
    public final Hardenable block;
    public final BlockPos pos;
    private final long hardenStart;
    private final long hardenUntil;
    
    private Hardening(final Hardenable block, final BlockPos pos) {
      this(block, pos, System.currentTimeMillis(), System.currentTimeMillis() + block.getHardeningTime() * 1000L);
    }
    
    private Hardening(final Hardenable block, final BlockPos pos, final long hardenStart, final long hardenUntil) {
      this.block = block;
      this.pos = pos;
      this.hardenStart = hardenStart;
      this.hardenUntil = hardenUntil;
    }
    
    public boolean isHardened() {
      return System.currentTimeMillis() >= this.hardenUntil;
    }
  }
}
