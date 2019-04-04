package lordmonoxide.gradient.recipes;

//TODO
/*
public class HardeningRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");
    final Age age = Age.get(JsonUtils.getInt(json, "age", 1));
    final int ticks = JsonUtils.getInt(json, "ticks");
    final boolean copyMeta = JsonUtils.getBoolean(json, "copy_meta", true);

    final Ingredient ingredient = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"), context);
    final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

    return new HardeningRecipe(group, age, ticks, copyMeta, output, ingredient);
  }
}
*/
