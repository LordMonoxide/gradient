package lordmonoxide.gradient.integrations.jei.alloy;

import lordmonoxide.gradient.recipes.AlloyRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlloyRecipeWrapper implements IRecipeWrapper {
  private final AlloyRecipe recipe;
  private final float scale;
  final Map<FluidStack, Integer> amounts = new HashMap<>();

  public AlloyRecipeWrapper(final AlloyRecipe recipe) {
    this.recipe = recipe;

    final int max = Math.max(recipe.getOutput().amount, Collections.max(recipe.getInputs(), Comparator.comparingInt(f -> f.amount)).amount);
    this.scale = (float)Fluid.BUCKET_VOLUME / max;
  }

  @Override
  public void getIngredients(final IIngredients ingredients) {
    this.amounts.clear();

    final List<List<FluidStack>> fluids = new ArrayList<>();
    for(final FluidStack fluid : this.recipe.getInputs()) {
      final FluidStack adjusted = new FluidStack(fluid, (int)(fluid.amount * this.scale));
      fluids.add(Collections.singletonList(adjusted));
      this.amounts.put(adjusted, fluid.amount);
    }

    final FluidStack output = this.recipe.getOutput();
    final FluidStack adjusted = new FluidStack(output, (int)(output.amount * this.scale));

    this.amounts.put(adjusted, output.amount);

    ingredients.setInputLists(VanillaTypes.FLUID, fluids);
    ingredients.setOutput(VanillaTypes.FLUID, adjusted);
  }
}
