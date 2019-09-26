package lordmonoxide.gradient.integrations.jei.melting;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;

public class MeltingRecipeCategory extends JeiRecipeCategory<MeltingRecipeWrapper> {
  public MeltingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.MELTING, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_melting.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.melting.name");
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final MeltingRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();

    guiItemStacks.init(0, true, 8, 39);
    guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

    guiFluidStacks.addTooltipCallback((slot, input, fluid, tooltip) -> tooltip.add(I18n.format("jei.melting.amount", fluid.amount)));
    guiFluidStacks.init(5, true, 47, 40);
    guiFluidStacks.set(5, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
  }
}
