package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.Tool;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class IngredientTool extends Ingredient {
  private final ItemStack stack;
  
  protected IngredientTool(GradientTools.Type type, GradientMetals.Metal metal) {
    super(Tool.getTool(type, metal, 1, 0));
    this.stack = Tool.getTool(type, metal, 1, 0);
  }
  
  protected IngredientTool(GradientTools.Type type) {
    this(type, GradientMetals.INVALID_METAL);
  }
  
  @Override
  public boolean apply(@Nullable ItemStack input) {
    return input != null && ItemStack.areItemStacksEqualUsingNBTShareTag(this.stack, input);
  }
}
