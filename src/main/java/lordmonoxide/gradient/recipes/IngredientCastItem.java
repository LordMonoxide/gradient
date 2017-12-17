package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.items.CastItem;
import net.minecraftforge.common.crafting.IngredientNBT;

public class IngredientCastItem extends IngredientNBT {
  protected IngredientCastItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal) {
    super(CastItem.getCastItem(cast, metal, 1));
  }
}
