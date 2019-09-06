package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MeltingRecipeFactory implements IRecipeFactory {
  @Override
  public IRecipe parse(final JsonContext context, final JsonObject json) {
    final String group = JsonUtils.getString(json, "group", "");

    final Ingredient ingredient = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"), context);
    final float meltTime = JsonUtils.getFloat(json, "melt_time");
    final float meltTemp = JsonUtils.getFloat(json, "melt_temp");

    final JsonObject result = JsonUtils.getJsonObject(json, "result");
    final String fluidName = JsonUtils.getString(result, "name");
    final Fluid fluid = FluidRegistry.getFluid(fluidName);

    if(fluid == null) {
      throw new JsonSyntaxException("Fluid " + fluidName + " not found");
    }

    final FluidStack output = new FluidStack(fluid, JsonUtils.getInt(result, "amount"));

    return new MeltingRecipe(group, meltTime, meltTemp, output, ingredient);
  }
}
