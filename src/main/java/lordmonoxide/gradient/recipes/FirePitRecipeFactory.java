package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class FirePitRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");
    final Age age = Age.get(JsonUtils.getInt(json, "age", 1));
    final int ticks = JsonUtils.getInt(json, "ticks");
    final float temperature = JsonUtils.getInt(json, "temperature");

    final Ingredient ingredient = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"), context);
    final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

    return new FirePitRecipe(group, age, ticks, temperature, output, ingredient);
  }
}
