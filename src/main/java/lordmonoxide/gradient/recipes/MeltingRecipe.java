package lordmonoxide.gradient.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MeltingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private final String group;
  private final float meltTime;
  private final float meltTemp;
  private final FluidStack output;
  private final Ingredient input;

  public MeltingRecipe(final String group, final float meltTime, final float meltTemp, final FluidStack output, final Ingredient input) {
    this.group = group;
    this.meltTime = meltTime;
    this.meltTemp = meltTemp;
    this.output = output;
    this.input = input;
  }

  public float getMeltTime() {
    return this.meltTime;
  }

  public float getMeltTemp() {
    return this.meltTemp;
  }

  public FluidStack getOutput() {
    return this.output;
  }

  public boolean matches(final ItemStack input) {
    return this.input.apply(input);
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World worldIn) {
    return false;
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
}
