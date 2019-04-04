package lordmonoxide.gradient.recipes;

//TODO
/*
public class FluidHandlerFactory implements IIngredientFactory {
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String fluidName = JsonUtils.getString(json, "fluid");
    final Fluid fluid = FluidRegistry.getFluid(fluidName);

    if(fluid == null) {
      throw new JsonSyntaxException("Unknown fluid '" + fluidName + '\'');
    }

    final int amount = JsonUtils.getInt(json, "amount", 1000);

    final FluidStack stack = new FluidStack(fluid, amount);

    return new FluidHandlerIngredient(stack);
  }
}
*/
