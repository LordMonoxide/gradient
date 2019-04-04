package lordmonoxide.gradient.recipes;

//TODO
/*
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

    final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

    return new FirePitRecipe(group, age, ticks, temperature, output, ingredients);
  }
}
*/
