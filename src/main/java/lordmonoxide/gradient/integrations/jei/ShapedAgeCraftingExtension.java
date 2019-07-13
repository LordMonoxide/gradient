package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import net.minecraftforge.common.util.Size2i;

import javax.annotation.Nullable;

public class ShapedAgeCraftingExtension extends ShapelessAgeCraftingExtension<AgeGatedShapedToolRecipe> {
  public ShapedAgeCraftingExtension(final AgeGatedShapedToolRecipe recipe) {
    super(recipe);
  }

  @Nullable
  @Override
  public Size2i getSize() {
    return new Size2i(this.recipe.getRecipeWidth(), this.recipe.getRecipeHeight());
  }
}
