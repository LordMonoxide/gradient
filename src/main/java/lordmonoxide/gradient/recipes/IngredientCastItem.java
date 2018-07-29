package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.init.CastRegistry;
import lordmonoxide.gradient.items.CastItem;
import net.minecraftforge.common.crafting.IngredientNBT;

public class IngredientCastItem extends IngredientNBT {
  protected IngredientCastItem(final CastRegistry.Cast cast, final GradientMetals.Metal metal) {
    super(CastItem.getCastItem(cast, metal, 1));
  }
}
