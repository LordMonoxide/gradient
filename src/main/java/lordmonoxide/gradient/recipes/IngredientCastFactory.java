package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.items.ItemClayCast;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class IngredientCastFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String castName = JsonUtils.getString(json, "cast");
    final GradientCasts.Cast cast = GradientCasts.getCast(castName);
    return new IngredientNBT(ItemClayCast.getCast(cast));
  }
}
