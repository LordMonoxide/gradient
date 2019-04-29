package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.recipes.GradientRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ShapelessAgeCraftingExtension<T extends GradientRecipe> implements ICraftingCategoryExtension {
  protected final T recipe;

  public ShapelessAgeCraftingExtension(final T recipe) {
    this.recipe = recipe;
  }

  @Nullable
  @Override
  public ResourceLocation getRegistryName() {
    return this.recipe.getId();
  }

  @Override
  public void drawInfo(final int recipeWidth, final int recipeHeight, final double mouseX, final double mouseY) {
    final FontRenderer font = Minecraft.getInstance().fontRenderer;

    final String age = I18n.format("jei.age." + this.recipe.getAge().value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 0, 62, 0x404040);
  }

  @Override
  public void setIngredients(final IIngredients ingredients) {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }
}
