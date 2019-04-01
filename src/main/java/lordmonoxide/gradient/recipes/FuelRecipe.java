package lordmonoxide.gradient.recipes;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class FuelRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final String group;
  public final int   duration;
  public final float ignitionTemp;
  public final float burnTemp;
  public final float heatPerSec;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public FuelRecipe(final String group, final int duration, final float ignitionTemp, final float burnTemp, final float heatPerSec, final Ingredient input) {
    this.group = group;
    this.duration = duration;
    this.ignitionTemp = ignitionTemp;
    this.burnTemp = burnTemp;
    this.heatPerSec = heatPerSec;
    this.input = NonNullList.from(Ingredient.EMPTY, input);
    this.isSimple = input.isSimple();
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
    recipeItemHelper.clear();
    inputStacks.clear();

    if(this.isSimple) {
      recipeItemHelper.accountStack(stack, 1);
      return recipeItemHelper.canCraft(this, null);
    }

    inputStacks.add(stack);
    return RecipeMatcher.findMatches(inputStacks, this.input) != null;
  }

  @Override
  @Deprecated
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.input.size();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.input;
  }
}
