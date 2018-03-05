package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
