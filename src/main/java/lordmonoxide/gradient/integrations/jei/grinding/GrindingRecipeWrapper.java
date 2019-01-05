package lordmonoxide.gradient.integrations.jei.grinding;

import lordmonoxide.gradient.recipes.GrindingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class GrindingRecipeWrapper implements IRecipeWrapper {
  private final IStackHelper stackHelper;
  private final GrindingRecipe recipe;

  public GrindingRecipeWrapper(final IStackHelper stackHelper, final GrindingRecipe recipe) {
    this.stackHelper = stackHelper;
    this.recipe = recipe;
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    final String age = I18n.format("jei.age." + this.recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 9, 8, 0x404040);

    font.drawString(I18n.format("jei.grinder.passes", this.recipe.passes), 9, 46, 0x404040);
    font.drawString(I18n.format("jei.grinder.ticks", this.recipe.ticks), 9, 60, 0x404040);
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, this.stackHelper.expandRecipeItemStackInputs(this.recipe.getIngredients()));
    ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getRecipeOutput());
  }
}
