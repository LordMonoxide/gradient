package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.items.GradientItemTool;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class ShapelessToolRecipe extends ShapelessRecipes {
  private static final Random rand = new Random();
  
  public ShapelessToolRecipe(final String group, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    super(group, output, ingredients);
  }
  
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
        list.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(stack));
      }
    }
    
    return list;
  }
}
