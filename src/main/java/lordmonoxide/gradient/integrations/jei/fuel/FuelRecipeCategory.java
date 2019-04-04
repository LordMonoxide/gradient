package lordmonoxide.gradient.integrations.jei.fuel;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import lordmonoxide.gradient.recipes.FuelRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FuelRecipeCategory extends JeiRecipeCategory<FuelRecipe> {
  public FuelRecipeCategory(final IGuiHelper guiHelper) {
    //TODO
    super(GradientRecipeCategoryUid.FUEL, FuelRecipe.class, null, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_fuel.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.fuel.name");
  }

  @Override
  public void setIngredients(final FuelRecipe recipe, final IIngredients ingredients) {

  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final FuelRecipe recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 4, 4);
    guiItemStacks.set(0, inputs.get(0));
  }
}
