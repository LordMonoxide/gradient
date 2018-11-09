package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.items.GradientItemTool;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;

public class AgeGatedShapelessToolRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static final Random rand = new Random();

  private final ShapelessRecipes recipe;
  public final Age age;

  public AgeGatedShapelessToolRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    this.recipe = new ShapelessRecipes(group, output, ingredients);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    return RecipeHelper.playerMeetsAgeRequirement(inv, this.age) && this.recipe.matches(inv, world);
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.recipe.getCraftingResult(inv);
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return this.recipe.canFit(width, height);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.recipe.getRecipeOutput();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
    final NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for(int i = 0; i < list.size(); ++i) {
      final ItemStack stack = inv.getStackInSlot(i);

      if(stack.getItem() instanceof GradientItemTool) {
        stack.attemptDamageItem(1, rand, null);

        if(stack.isItemStackDamageable() && stack.getItemDamage() > stack.getMaxDamage()) {
          list.set(i, ItemStack.EMPTY);
        } else {
          list.set(i, stack.copy());
        }
      } else {
        list.set(i, ForgeHooks.getContainerItem(stack));
      }
    }

    return list;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.recipe.getIngredients();
  }

  @Override
  public boolean isDynamic() {
    return this.recipe.isDynamic();
  }

  @Override
  public String getGroup() {
    return this.recipe.getGroup();
  }
}
