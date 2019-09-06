package lordmonoxide.gradient.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class AlloyRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");

    final NonNullList<FluidStack> ingredients = NonNullList.create();
    for(final JsonElement element : JsonUtils.getJsonArray(json, "ingredients")) {
      final JsonObject obj = (JsonObject)element;
      final String fluidName = JsonUtils.getString(obj, "name");
      final Fluid fluid = FluidRegistry.getFluid(fluidName);

      if(fluid == null) {
        throw new JsonSyntaxException("Fluid " + fluidName + " not found");
      }

      ingredients.add(new FluidStack(fluid, JsonUtils.getInt(obj, "amount")));
    }

    if(ingredients.isEmpty()) {
      throw new JsonParseException("No ingredients for alloy recipe");
    }

    final JsonObject result = JsonUtils.getJsonObject(json, "result");
    final String fluidName = JsonUtils.getString(result, "name");
    final Fluid fluid = FluidRegistry.getFluid(fluidName);

    if(fluid == null) {
      throw new JsonSyntaxException("Fluid " + fluidName + " not found");
    }

    final FluidStack output = new FluidStack(fluid, JsonUtils.getInt(result, "amount"));

    return new AlloyRecipe(group, output, ingredients);
  }
}
