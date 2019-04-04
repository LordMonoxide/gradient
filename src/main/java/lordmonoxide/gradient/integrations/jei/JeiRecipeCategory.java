package lordmonoxide.gradient.integrations.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

public abstract class JeiRecipeCategory<RECIPE extends IRecipe> implements IRecipeCategory<RECIPE> {
  private final ResourceLocation uid;
  private final Class<RECIPE> recipeClass;
  private final IDrawable icon;
  private final IDrawable background;

  protected JeiRecipeCategory(final ResourceLocation uid, final Class<RECIPE> recipeClass, final IDrawable icon, final IDrawable background) {
    this.uid = uid;
    this.recipeClass = recipeClass;
    this.icon = icon;
    this.background = background;
  }

  @Override
  public ResourceLocation getUid() {
    return this.uid;
  }

  @Override
  public String getTitle() {
    return "Gradient";
  }

  @Override
  public IDrawable getIcon() {
    return this.icon;
  }

  @Override
  public IDrawable getBackground() {
    return this.background;
  }

  @Override
  public Class<RECIPE> getRecipeClass() {
    return this.recipeClass;
  }
}
