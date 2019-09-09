package lordmonoxide.gradient.integrations.jei.alloy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class AlloyRecipeCategory extends JeiRecipeCategory<AlloyRecipeWrapper> {
  public AlloyRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.ALLOY, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_alloy.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.alloy.name");
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final AlloyRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
    guiFluidStacks.addTooltipCallback((slot, input, fluid, tooltip) -> tooltip.add(I18n.format("jei.alloy.amount", recipeWrapper.amounts.get(fluid))));

    final List<List<FluidStack>> inputs = ingredients.getInputs(VanillaTypes.FLUID);

    for(int slot = 0; slot < inputs.size(); slot++) {
      guiFluidStacks.init(slot, true, 4 + slot * 20, 26);
      guiFluidStacks.set(slot, inputs.get(slot));
    }

    guiFluidStacks.init(5, true, 122, 26);
    guiFluidStacks.set(5, ingredients.getOutputs(VanillaTypes.FLUID).get(0));
  }
}
