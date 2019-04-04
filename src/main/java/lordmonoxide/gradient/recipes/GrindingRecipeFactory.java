package lordmonoxide.gradient.recipes;

//TODO
/*
public class GrindingRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");
    final Age age = Age.get(JsonUtils.getInt(json, "age", 1));
    final int passes = JsonUtils.getInt(json, "passes");
    final int ticks = JsonUtils.getInt(json, "ticks");

    final NonNullList<Ingredient> ingredients = NonNullList.create();
    for(final JsonElement element : JsonUtils.getJsonArray(json, "ingredients")) {
      ingredients.add(CraftingHelper.getIngredient(element, context));
    }

    if(ingredients.isEmpty()) {
      throw new JsonParseException("No ingredients for grinding recipe");
    }

    final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

    return new GrindingRecipe(group, age, passes, ticks, output, ingredients);
  }
}
*/
