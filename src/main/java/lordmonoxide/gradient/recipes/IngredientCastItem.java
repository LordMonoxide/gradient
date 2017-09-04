package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.items.CastItem;
import net.minecraftforge.common.crafting.IngredientNBT;

public class IngredientCastItem extends IngredientNBT {
  protected IngredientCastItem(GradientCasts.Cast cast, GradientMetals.Metal metal) {
    super(CastItem.getCastItem(cast, metal));
  }
}
