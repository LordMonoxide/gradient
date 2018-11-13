package lordmonoxide.gradient.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
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

    final NonNullList<Ingredient> ingredients = NonNullList.create();
    for(final JsonElement element : JsonUtils.getJsonArray(json, "ingredients")) {
      ingredients.add(CraftingHelper.getIngredient(element, context));
    }

    if(ingredients.isEmpty()) {
      throw new JsonParseException("No ingredients for mixing recipe");
    }

    ingredients.add(Ingredient.fromItem(GradientItems.FIREPIT_DISCRIMINATOR));

    final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

    return new FirePitRecipe(group, age, ticks, temperature, output, ingredients);
  }
}
