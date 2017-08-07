package lordmonoxide.gradient;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
    
    this.add(Items.PORKCHOP, Items.COOKED_PORKCHOP, 120, 200);
    this.add(Items.BEEF,     Items.COOKED_BEEF,     120, 200);
    this.add(Items.CHICKEN,  Items.COOKED_CHICKEN,  120, 200);
    this.add(Items.RABBIT,   Items.COOKED_RABBIT,   120, 200);
    this.add(Items.MUTTON,   Items.COOKED_MUTTON,   120, 200);
    this.add(Items.POTATO,   Items.BAKED_POTATO,    120, 200);
  }
  
  public void add(Item raw, Item cooked, int duration, float cookTemp) {
    this.add(raw.getDefaultInstance(), cooked.getDefaultInstance(), duration, cookTemp);
  }
  
  public void add(ItemStack raw, ItemStack cooked, int duration, float cookTemp) {
    this.add(raw, new Food(cooked, duration, cookTemp));
  }
  
  public void add(ItemStack raw, Food food) {
    this.foods.put(raw, food);
  }
  
  public Food get(ItemStack raw) {
    for(Map.Entry<ItemStack, Food> entry : this.foods.entrySet()) {
      if(ItemStack.areItemsEqual(raw, entry.getKey())) {
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
