package lordmonoxide.gradient.integrations.jei.crafting;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.crafting.IRecipe;

public class RecipeWrapper<T extends IRecipe> implements IRecipeWrapper {
  protected final IStackHelper stackHelper;
  protected final T recipe;

  public RecipeWrapper(final IStackHelper stackHelper, final T recipe) {
    this.stackHelper = stackHelper;
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, this.stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients()));
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
  }
}
