package lordmonoxide.gradient.integrations.jei.melting;

import lordmonoxide.gradient.recipes.MeltingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class MeltingRecipeWrapper implements IRecipeWrapper {
  private final IStackHelper stackHelper;
  private final MeltingRecipe recipe;

  public MeltingRecipeWrapper(final IStackHelper stackHelper, final MeltingRecipe recipe) {
    this.stackHelper = stackHelper;
    this.recipe = recipe;
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    font.drawString(I18n.format("jei.melting.meltTime", this.recipe.getMeltTime()), 5,  5, 0x404040);
    font.drawString(I18n.format("jei.melting.meltTemp", this.recipe.getMeltTemp()), 5, 16, 0x404040);
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, this.stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients()));
    ingredients.setOutput(VanillaTypes.FLUID, this.recipe.getOutput());
  }
}
