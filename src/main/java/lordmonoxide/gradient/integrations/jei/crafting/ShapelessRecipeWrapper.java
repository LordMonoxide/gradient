package lordmonoxide.gradient.integrations.jei.crafting;

import lordmonoxide.gradient.recipes.AgeGatedShapelessToolRecipe;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

public class ShapelessRecipeWrapper extends RecipeWrapper<AgeGatedShapelessToolRecipe> {
  public ShapelessRecipeWrapper(final IStackHelper stackHelper, final AgeGatedShapelessToolRecipe recipe) {
    super(stackHelper, recipe);
  }

  @Override
  public void drawInfo(final Minecraft minecraft, final int recipeWidth, final int recipeHeight, final int mouseX, final int mouseY) {
    final FontRenderer font = minecraft.fontRenderer;

    final String age = I18n.format("jei.age." + this.recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 0, 62, 0x404040);
  }
}
