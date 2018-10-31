package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AgeGatedShapedRecipe extends ShapedRecipes {
  private final Age age;

  public AgeGatedShapedRecipe(final String group, final Age age, final int width, final int height, final NonNullList<Ingredient> ingredients, final ItemStack result) {
    super(group, width, height, ingredients, result);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    return RecipeHelper.playerMeetsAgeRequirement(inv, this.age) && super.matches(inv, world);
  }
}
