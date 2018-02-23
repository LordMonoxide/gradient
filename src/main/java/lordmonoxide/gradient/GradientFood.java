package lordmonoxide.gradient;

import ic2.api.item.IC2Items;
import lordmonoxide.gradient.blocks.claybucket.ItemClayBucket;
import lordmonoxide.gradient.items.GradientItem;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.HashMap;
import java.util.Map;

public final class GradientFood {
  private GradientFood() { }
  
  private static final Fluid WATER = FluidRegistry.getFluid("water");

  public static final Food INVALID_FOOD = new Food(ItemStack.EMPTY, 0, Integer.MAX_VALUE);
  
  private static final Map<ItemStack, Food> foods = new HashMap<>();
  
  static {
    FurnaceRecipes.instance().getSmeltingList().entrySet().stream()
      .filter(e -> e.getKey().getItem() instanceof ItemFood)
      .forEach(e -> add(e.getKey(), e.getValue(), 120, 200));
    
    add(Items.PORKCHOP, Items.COOKED_PORKCHOP, 120, 200);
    add(Items.BEEF,     Items.COOKED_BEEF,     120, 200);
    add(Items.CHICKEN,  Items.COOKED_CHICKEN,  120, 200);
    add(Items.RABBIT,   Items.COOKED_RABBIT,   120, 200);
    add(Items.MUTTON,   Items.COOKED_MUTTON,   120, 200);
    add(Items.POTATO,   Items.BAKED_POTATO,    120, 200);
    
    add(IC2Items.getItem("misc_resource", "resin"), IC2Items.getItem("crafting", "rubber"), 30, 200);
    add(GradientItems.SUGARCANE_PASTE, new ItemStack(Items.SUGAR, 2), 30, 200);
    add(FluidUtil.getFilledBucket(new FluidStack(WATER, Fluid.BUCKET_VOLUME)), GradientItems.SALT.getItemStack(), 30, 200);
    add(ItemClayBucket.getFilledBucket(WATER), GradientItems.SALT.getItemStack(), 30, 200);
    add(GradientItems.DOUGH, new ItemStack(Items.BREAD, 4), 120, 200);
  }
  
  public static void add(final Item raw, final Item cooked, final int duration, final float cookTemp) {
    add(new ItemStack(raw), new ItemStack(cooked), duration, cookTemp);
  }
  
  public static void add(final Item raw, final ItemStack cooked, final int duration, final float cookTemp) {
    add(new ItemStack(raw), cooked, duration, cookTemp);
  }

  public static void add(final ItemStack raw, final ItemStack cooked, final int duration, final float cookTemp) {
    add(raw, new Food(cooked, duration, cookTemp));
  }
  
  public static void add(final ItemStack raw, final Food food) {
    foods.put(raw, food);
  }
  
  public static Food get(final ItemStack raw) {
    for(final Map.Entry<ItemStack, Food> entry : foods.entrySet()) {
      if(ItemStack.areItemStacksEqual(raw, entry.getKey())) {
        return entry.getValue();
      }
    }
    
    return INVALID_FOOD;
  }
  
  public static boolean has(final ItemStack itemStack) {
    return get(itemStack) != INVALID_FOOD;
  }
  
  public static class Food {
    public final ItemStack cooked;
    public final int       duration;
    public final float     cookTemp;
    
    public Food(final ItemStack cooked, final int duration, final float cookTemp) {
      this.cooked   = cooked;
      this.duration = duration;
      this.cookTemp = cookTemp;
    }
  }
  
  public static final class CookingFood {
    public final GradientFood.Food food;
    private final int cookTicksTotal;
    private int cookTicks;

    public static CookingFood fromNbt(final GradientFood.Food food, final NBTTagCompound tag) {
      CookingFood cooking = new CookingFood(food, tag.getInteger("ticksTotal"));
      cooking.cookTicks = tag.getInteger("ticks");
      return cooking;
    }
    
    public CookingFood(final GradientFood.Food food) {
      this(food, food.duration * 20);
    }
    
    private CookingFood(final Food food, final int cookTicksTotal) {
      this.food = food;
      this.cookTicksTotal = cookTicksTotal;
    }

    public CookingFood tick() {
      this.cookTicks++;
      return this;
    }
    
    public boolean isCooked() {
      return this.cookTicks >= this.cookTicksTotal;
    }
    
    public float cookPercent() {
      return (float)this.cookTicks / this.cookTicksTotal;
    }

    public NBTTagCompound writeToNbt(final NBTTagCompound tag) {
      tag.setInteger("ticksTotal", this.cookTicksTotal);
      tag.setInteger("ticks", this.cookTicks);
      return tag;
    }
  }
}
