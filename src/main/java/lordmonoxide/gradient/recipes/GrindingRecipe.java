package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class GrindingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private final String group;
  public final Age age;
  public final int passes;
  public final int ticks;
  private final ItemStack output;
  private final Ingredient input;
  private final NonNullList<Ingredient> inputs;

  public GrindingRecipe(final String group, final Age age, final int passes, final int ticks, final ItemStack output, final Ingredient input) {
    this.group = group;
    this.age = age;
    this.passes = passes;
    this.ticks = ticks;
    this.output = output;
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

  public boolean matches(final ItemStack input) {
    return this.input.apply(input);
  }

  public boolean matches(final ItemStack input, final Age age) {
    if(age.ordinal() < this.age.ordinal()) {
      return false;
    }

    return this.matches(input);
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.output.copy();
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.inputs.size();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.output;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.inputs;
  }
}
