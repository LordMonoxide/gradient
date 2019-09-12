package lordmonoxide.gradient.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AlloyRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private final String group;
  private final FluidStack output;
  private final NonNullList<FluidStack> inputs;
  private final Map<Fluid, FluidStack> inputMap;

  public AlloyRecipe(final String group, final FluidStack output, final NonNullList<FluidStack> inputs) {
    this.group = group;
    this.output = output;
    this.inputs = inputs;
    this.inputMap = this.unifyFluids(inputs);
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World worldIn) {
    return false;
  }

  public boolean matches(final NonNullList<FluidStack> inputs) {
    final Map<Fluid, FluidStack> inputMap = this.unifyFluids(inputs);

    // Make sure we have all required fluids
    for(final FluidStack required : this.inputs) {
      if(!inputMap.containsKey(required.getFluid()) || inputMap.get(required.getFluid()).amount < required.amount) {
        return false;
      }
    }

    // Make sure there are no extra fluids
    for(final FluidStack required : inputs) {
      if(!this.inputMap.containsKey(required.getFluid())) {
        return false;
      }
    }

    return true;
  }

  public Collection<FluidStack> getInputs() {
    return this.inputMap.values();
  }

  public FluidStack getOutput() {
    return this.output;
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return false;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
    return NonNullList.create();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.create();
  }

  @Override
  public boolean isDynamic() {
    return false;
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  private Map<Fluid, FluidStack> unifyFluids(final NonNullList<FluidStack> inputs) {
    final Map<Fluid, FluidStack> outputs = new HashMap<>();

    for(final FluidStack fluidStack : inputs) {
      outputs.computeIfAbsent(fluidStack.getFluid(), fluid -> new FluidStack(fluid, 0)).amount += fluidStack.amount;
    }

    return outputs;
  }
}
