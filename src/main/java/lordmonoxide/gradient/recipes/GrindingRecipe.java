package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
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

public class GrindingRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final String group;
  private final Age age;
  private final ItemStack output;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public GrindingRecipe(final String group, final Age age, final ItemStack output, final Ingredient input) {
    this.group = group;
    this.age = age;
    this.output = output;
    this.input = NonNullList.from(Ingredient.EMPTY, input);
    this.isSimple = input.isSimple();
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    if(!RecipeHelper.playerMeetsAgeRequirement(inv, this.age)) {
      return false;
    }

    final ItemStack itemstack = inv.getStackInSlot(0);

    if(itemstack.isEmpty()) {
      return false;
    }

    if(this.isSimple) {
      recipeItemHelper.clear();
      recipeItemHelper.accountStack(itemstack, 1);
      return recipeItemHelper.canCraft(this, null);
    }

    inputStacks.clear();
    inputStacks.add(itemstack);
    return RecipeMatcher.findMatches(inputStacks, this.input) != null;
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.output.copy();
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= 1;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.output;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.input;
  }
}
