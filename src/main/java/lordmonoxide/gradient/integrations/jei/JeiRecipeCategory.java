package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public abstract class JeiRecipeCategory<WRAPPER extends IRecipeWrapper> implements IRecipeCategory<WRAPPER> {
  private final IDrawable background;
  private final String uid;

  protected JeiRecipeCategory(final String uid, final IDrawable background) {
    this.uid = uid;
    this.background = background;
  }

  @Override
  public String getUid() {
    return this.uid;
  }

  @Override
  public String getModName() {
    return GradientMod.NAME;
  }

  @Override
  public IDrawable getBackground() {
    return this.background;
  }
}
