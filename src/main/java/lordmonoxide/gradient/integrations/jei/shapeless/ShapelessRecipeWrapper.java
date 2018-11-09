package lordmonoxide.gradient.integrations.jei.shapeless;

import lordmonoxide.gradient.recipes.AgeGatedShapelessToolRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class ShapelessRecipeWrapper implements IRecipeWrapper {
  private final IStackHelper stackHelper;
  private final AgeGatedShapelessToolRecipe recipe;

  public ShapelessRecipeWrapper(final IStackHelper stackHelper, final AgeGatedShapelessToolRecipe recipe) {
    this.stackHelper = stackHelper;
    this.recipe = recipe;
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    final String age = I18n.format("jei.age." + this.recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 0, 62, 0x404040);
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, this.stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients()));
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
  }
}
