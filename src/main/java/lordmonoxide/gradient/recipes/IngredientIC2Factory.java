package lordmonoxide.gradient.recipes;

//TODO
/*
public class IngredientIC2Factory implements IIngredientFactory {
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String name = JsonUtils.getString(json, "name");
    final String variant = JsonUtils.getString(json, "variant", "");

    final ItemStack stack = IC2Items.getItem(name, variant);

    if(stack == null || stack.isEmpty()) {
      if(variant.isEmpty()) {
        throw new JsonSyntaxException("No IC2 item " + name);
      }

      throw new JsonSyntaxException("No IC2 item " + name + ':' + variant);
    }

    return Ingredient.fromStacks(stack);
  }
}
*/
