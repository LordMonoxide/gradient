package lordmonoxide.gradient.integrations.jei.shaped;

import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class ShapedRecipeWrapper implements IShapedCraftingRecipeWrapper {
  private final IStackHelper stackHelper;
  private final AgeGatedShapedToolRecipe recipe;

  public ShapedRecipeWrapper(final IStackHelper stackHelper, final AgeGatedShapedToolRecipe recipe) {
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

  @Override
  public int getWidth() {
    return this.recipe.getRecipeWidth();
  }

  @Override
  public int getHeight() {
    return this.recipe.getRecipeHeight();
  }
}
