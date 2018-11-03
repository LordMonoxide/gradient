package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.blocks.manualgrinder.ContainerManualGrinder;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.Container;
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
  public final Age age;
  public final int passes;
  public final int ticks;
  private final ItemStack output;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public GrindingRecipe(final String group, final Age age, final int passes, final int ticks, final ItemStack output, final NonNullList<Ingredient> input) {
    this.group = group;
    this.age = age;
    this.passes = passes;
    this.ticks = ticks;
    this.output = output;
    this.input = input;

    boolean isSimple = true;
    for(final Ingredient ingredient : input) {
      isSimple &= ingredient.isSimple();
    }

    this.isSimple = isSimple;
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    final Container container = RecipeHelper.getContainer(inv);

    if(!(container instanceof ContainerManualGrinder)) {
      return false;
    }

    if(((ContainerManualGrinder)container).getPlayerAge().ordinal() < this.age.ordinal()) {
      return false;
    }

    recipeItemHelper.clear();
    inputStacks.clear();

    int ingredientCount = 0;
    for(int y = 0; y < inv.getHeight(); ++y) {
      for(int x = 0; x < inv.getWidth(); ++x) {
        final ItemStack itemstack = inv.getStackInRowAndColumn(x, y);

        if(!itemstack.isEmpty()) {
          ++ingredientCount;

          if(this.isSimple) {
            recipeItemHelper.accountStack(itemstack, 1);
          } else {
            inputStacks.add(itemstack);
          }
        }
      }
    }

    if(ingredientCount != this.input.size()) {
      return false;
    }

    if(this.isSimple) {
      return recipeItemHelper.canCraft(this, null);
    }

    return RecipeMatcher.findMatches(inputStacks, this.input) != null;
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.output.copy();
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.input.size();
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
