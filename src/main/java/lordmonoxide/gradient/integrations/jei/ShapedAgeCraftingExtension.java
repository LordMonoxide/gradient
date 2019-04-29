package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.IShapedCraftingCategoryExtension;

public class ShapedAgeCraftingExtension extends ShapelessAgeCraftingExtension<AgeGatedShapedToolRecipe> implements IShapedCraftingCategoryExtension {
  public ShapedAgeCraftingExtension(final AgeGatedShapedToolRecipe recipe) {
    super(recipe);
  }

  @Override
  public int getWidth() {
    return this.recipe.getRecipeWidth();
  }

  @Override
  public int getHeight() {
    return this.recipe.getRecipeHeight();
  }
}
