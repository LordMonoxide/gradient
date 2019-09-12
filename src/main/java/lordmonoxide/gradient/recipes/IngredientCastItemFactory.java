package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class IngredientCastItemFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String castName  = JsonUtils.getString(json, "cast");
    final String metalName = JsonUtils.getString(json, "metal");

    final GradientCasts.Cast   cast  = GradientCasts.getCast(castName);
    final Metal metal = Metals.get(metalName);

    if(metal == Metals.INVALID_METAL) {
      throw new JsonSyntaxException("Unknown metal '" + metalName + '\'');
    }

    return new IngredientCastItem(cast, metal);
  }
}
