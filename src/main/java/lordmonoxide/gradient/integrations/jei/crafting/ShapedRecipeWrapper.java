package lordmonoxide.gradient.integrations.jei.crafting;

import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class ShapedRecipeWrapper extends RecipeWrapper<AgeGatedShapedToolRecipe> implements IShapedCraftingRecipeWrapper {
  public ShapedRecipeWrapper(final IStackHelper stackHelper, final AgeGatedShapedToolRecipe recipe) {
    super(stackHelper, recipe);
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    final String age = I18n.format("jei.age." + this.recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 0, 62, 0x404040);
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
