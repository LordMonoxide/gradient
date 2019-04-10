package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraftforge.common.crafting.IngredientNBT;

public class IngredientCastItem extends IngredientNBT {
  protected IngredientCastItem(final GradientCasts.Cast cast, final Metal metal) {
    super(GradientItems.castItem(cast, metal, 1));
  }
}
