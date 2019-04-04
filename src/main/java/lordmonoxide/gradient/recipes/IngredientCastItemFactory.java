package lordmonoxide.gradient.recipes;

//TODO
/*
public class IngredientCastItemFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String castName  = JsonUtils.getString(json, "cast");
    final String metalName = JsonUtils.getString(json, "metal");

    final GradientCasts.Cast   cast  = GradientCasts.getCast(castName);
    final GradientMetals.Metal metal = GradientMetals.getMetal(metalName);

    if(metal == GradientMetals.INVALID_METAL) {
      throw new JsonSyntaxException("Unknown metal '" + metalName + '\'');
    }

    return new IngredientCastItem(cast, metal);
  }
}
*/
