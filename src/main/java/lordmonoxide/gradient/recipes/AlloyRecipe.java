package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class AlloyRecipe extends ShapelessRecipes {
  public AlloyRecipe(final String group, final GradientMetals.Alloy alloy) {
    super(group, GradientMetals.getBucket(alloy.output), NonNullList.from(null, alloy.inputs.stream().map(GradientMetals::getBucket).map(IngredientNBT::new).toArray(Ingredient[]::new)));
  }
  
  @Override
  public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
    return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
  }
}
