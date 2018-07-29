package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.init.CastRegistry;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class IngredientCastItemFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String castName  = JsonUtils.getString(json, "cast");
    final String metalName = JsonUtils.getString(json, "metal");

    final CastRegistry.Cast cast = GameRegistry.findRegistry(CastRegistry.Cast.class).getValue(new ResourceLocation(castName));
    final GradientMetals.Metal metal = GradientMetals.getMetal(metalName);

    if(metal == GradientMetals.INVALID_METAL) {
      throw new JsonSyntaxException("Unknown metal '" + metalName + '\'');
    }

    return new IngredientCastItem(cast, metal);
  }
}
