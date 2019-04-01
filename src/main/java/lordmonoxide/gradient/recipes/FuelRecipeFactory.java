package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class FuelRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");
    final int   duration = JsonUtils.getInt(json, "duration");
    final float ignitionTemp = JsonUtils.getFloat(json, "ignitionTemp");
    final float burnTemp = JsonUtils.getFloat(json, "burnTemp");
    final float heatPerSec = JsonUtils.getFloat(json, "heatPerSec");
    final Ingredient ingredient = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"), context);

    return new FuelRecipe(group, duration, ignitionTemp, burnTemp, heatPerSec, ingredient);
  }
}
