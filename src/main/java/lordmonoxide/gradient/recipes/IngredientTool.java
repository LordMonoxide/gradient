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
    super(Tool.getTool(type, metal));
    this.stack = Tool.getTool(type, metal);
  }
  
  protected IngredientTool(GradientTools.Type type) {
    this(type, GradientMetals.INVALID_METAL);
  }
  
  @Override
  public boolean apply(@Nullable ItemStack input) {
    if(input == null) {
      return false;
    }
    
    final ItemStack stack1 = this.stack.copy();
    final ItemStack stack2 = input.copy();
    
    if(Tool.getMetal(this.stack) == GradientMetals.INVALID_METAL) {
      if(stack2.hasTagCompound()) {
        stack1.getTagCompound().removeTag("metal");
        stack2.getTagCompound().removeTag("metal");
      }
    }
    
    return ItemStack.areItemStacksEqualUsingNBTShareTag(stack1, stack2);
  }
}
