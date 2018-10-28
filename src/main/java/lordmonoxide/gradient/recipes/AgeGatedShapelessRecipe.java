package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AgeGatedShapelessRecipe extends ShapelessRecipes {
  private final Age age;

  public AgeGatedShapelessRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    super(group, output, ingredients);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    return RecipeHelper.playerMeetsAgeRequirement(inv, this.age) && super.matches(inv, world);
  }
}
