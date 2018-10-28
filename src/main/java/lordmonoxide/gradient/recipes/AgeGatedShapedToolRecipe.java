package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.items.GradientItemTool;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class AgeGatedShapedToolRecipe extends ShapedRecipes {
  private static final Random rand = new Random();

  private final Age age;

  public AgeGatedShapedToolRecipe(final String group, final Age age, final int width, final int height, final NonNullList<Ingredient> ingredients, final ItemStack result) {
    super(group, width, height, ingredients, result);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    return RecipeHelper.playerMeetsAgeRequirement(inv, this.age) && super.matches(inv, world);
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
}
