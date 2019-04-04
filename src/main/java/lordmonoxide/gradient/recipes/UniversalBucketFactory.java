package lordmonoxide.gradient.recipes;

//TODO
/*
public class UniversalBucketFactory implements IIngredientFactory {
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String fluidName = JsonUtils.getString(json, "fluid");
    final Fluid fluid = FluidRegistry.getFluid(fluidName);

    if(fluid == null) {
      throw new JsonSyntaxException("Unknown fluid '" + fluidName + '\'');
    }

    final ItemStack filledBucket = FluidUtil.getFilledBucket(new FluidStack(fluid, 0));

    if(filledBucket.isEmpty()) {
      throw new JsonSyntaxException("No bucket registered for fluid '" + fluidName + '\'');
    }

    return new IngredientNBT(filledBucket);
  }
}
*/
