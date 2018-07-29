package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.init.CastRegistry;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class IngredientCastFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String castName = JsonUtils.getString(json, "cast");
    final CastRegistry.Cast cast = GameRegistry.findRegistry(CastRegistry.Cast.class).getValue(new ResourceLocation(castName));
    return new IngredientNBT(ItemClayCast.getCast(cast));
  }
}
