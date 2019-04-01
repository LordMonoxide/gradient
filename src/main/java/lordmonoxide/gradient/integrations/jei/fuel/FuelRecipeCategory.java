package lordmonoxide.gradient.integrations.jei.fuel;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FuelRecipeCategory extends JeiRecipeCategory<FuelRecipeWrapper> {
  public FuelRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.FUEL, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_fuel.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.fuel.name");
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final FuelRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 4, 4);
    guiItemStacks.set(0, inputs.get(0));
  }
}
