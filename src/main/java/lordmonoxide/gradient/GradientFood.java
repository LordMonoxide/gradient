package lordmonoxide.gradient;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.HashMap;
import java.util.Map;

public final class GradientFood {
  private GradientFood() { }
  
  public static final Food INVALID_FOOD = new Food(ItemStack.EMPTY, 0, 0);
  
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
  }
  
  public static void add(final Item raw, final Item cooked, final int duration, final float cookTemp) {
    add(new ItemStack(raw), new ItemStack(cooked), duration, cookTemp);
  }
  
  public static void add(final ItemStack raw, final ItemStack cooked, final int duration, final float cookTemp) {
    add(raw, new Food(cooked, duration, cookTemp));
  }
  
  public static void add(final ItemStack raw, final Food food) {
    foods.put(raw, food);
  }
  
  public static Food get(final ItemStack raw) {
    for(Map.Entry<ItemStack, Food> entry : foods.entrySet()) {
      if(ItemStack.areItemsEqual(raw, entry.getKey())) {
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
    public final long cookStart;
    public final long cookUntil;
    
    public CookingFood(final GradientFood.Food food) {
      this(food, System.currentTimeMillis(), System.currentTimeMillis() + food.duration * 1000L);
    }
    
    public CookingFood(final GradientFood.Food food, final long cookStart, final long cookUntil) {
      this.food = food;
      this.cookStart = cookStart;
      this.cookUntil = cookUntil;
    }
    
    public boolean isCooked() {
      return System.currentTimeMillis() >= this.cookUntil;
    }
    
    public float cookPercent() {
      return (float)(System.currentTimeMillis() - this.cookStart) / (this.cookUntil - this.cookStart);
    }
  }
}
