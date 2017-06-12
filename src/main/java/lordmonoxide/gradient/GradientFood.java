package lordmonoxide.gradient;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.HashMap;
import java.util.Map;

public final class GradientFood {
  public static final GradientFood instance = new GradientFood();
  
  public static final Food INVALID_FOOD = new Food(ItemStack.EMPTY, 0, 0);
  
  private final Map<ItemStack, Food> foods = new HashMap<>();
  
  private GradientFood() {
    for(Map.Entry<ItemStack, ItemStack> e : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
      if(e.getKey().getItem() instanceof ItemFood) {
        this.add(e.getKey(), e.getValue(), 120, 200);
      }
    }
  }
  
  public void add(ItemStack item, ItemStack cooked, int duration, float cookTemp) {
    this.add(item, new Food(cooked, duration, cookTemp));
  }
  
  public void add(ItemStack item, Food food) {
    this.foods.put(item, food);
  }
  
  public Food get(ItemStack item) {
    for(Map.Entry<ItemStack, Food> entry : this.foods.entrySet()) {
      if(ItemStack.areItemStacksEqual(item, entry.getKey())) {
        return entry.getValue();
      }
    }
    
    return INVALID_FOOD;
  }
  
  public boolean has(ItemStack itemStack) {
    return this.get(itemStack) != INVALID_FOOD;
  }
  
  public static class Food {
    public final ItemStack cooked;
    public final int       duration;
    public final float     cookTemp;
    
    public Food(ItemStack cooked, int duration, float cookTemp) {
      this.cooked   = cooked;
      this.duration = duration;
      this.cookTemp = cookTemp;
    }
  }
}
