package lordmonoxide.gradient.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class FuelRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private final String group;
  public final int duration;
  public final float ignitionTemp;
  public final float burnTemp;
  public final float heatPerSec;
  private final Ingredient input;
  private final NonNullList<Ingredient> inputs;

  public FuelRecipe(final String group, final int duration, final float ignitionTemp, final float burnTemp, final float heatPerSec, final Ingredient input) {
    this.group = group;
    this.duration = duration;
    this.ignitionTemp = ignitionTemp;
    this.burnTemp = burnTemp;
    this.heatPerSec = heatPerSec;
    this.input = input;
    this.inputs = NonNullList.from(Ingredient.EMPTY, input);
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  @Override
  @Deprecated
  public boolean matches(final InventoryCrafting inv, final World world) {
    return false;
  }

  public boolean matches(final ItemStack stack) {
    return this.input.apply(stack);
  }

  @Override
  @Deprecated
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.inputs.size();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.inputs;
  }
}
