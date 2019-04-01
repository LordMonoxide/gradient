package lordmonoxide.gradient.integrations.jei.fuel;

import lordmonoxide.gradient.recipes.FuelRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class FuelRecipeWrapper implements IRecipeWrapper {
  private final IStackHelper stackHelper;
  private final FuelRecipe recipe;

  public FuelRecipeWrapper(final IStackHelper stackHelper, final FuelRecipe recipe) {
    this.stackHelper = stackHelper;
    this.recipe = recipe;
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    font.drawString(I18n.format("jei.fuel.duration", this.recipe.duration), 5,  25, 0x404040);
    font.drawString(I18n.format("jei.fuel.ignitionTemp", this.recipe.ignitionTemp), 5, 36, 0x404040);
    font.drawString(I18n.format("jei.fuel.burnTemp", this.recipe.burnTemp), 5, 47, 0x404040);
    font.drawString(I18n.format("jei.fuel.heatPerSec", this.recipe.heatPerSec), 5, 58, 0x404040);
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, this.stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients()));
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
  }
}
