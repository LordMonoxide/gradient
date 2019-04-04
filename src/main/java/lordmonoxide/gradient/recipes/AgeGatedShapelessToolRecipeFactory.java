package lordmonoxide.gradient.recipes;

//TODO
/*
public class AgeGatedShapelessToolRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");
    final Age age = Age.get(JsonUtils.getInt(json, "age", 1));

    final NonNullList<Ingredient> ings = NonNullList.create();
    for(final JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
      ings.add(CraftingHelper.getIngredient(ele, context));
    }

    if(ings.isEmpty()) {
      throw new JsonParseException("No ingredients for shapeless recipe");
    }

    if(ings.size() > 9) {
      throw new JsonParseException("Too many ingredients for shapeless recipe");
    }

    final ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
    return new AgeGatedShapelessToolRecipe(group, age, itemstack, ings);
  }
}
*/
