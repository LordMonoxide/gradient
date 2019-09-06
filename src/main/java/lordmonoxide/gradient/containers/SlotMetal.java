package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.recipes.MeltingRecipe;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotMetal extends SlotItemHandler {
  public SlotMetal(final IItemHandler inventory, final int slotNumber, final int x, final int y) {
    super(inventory, slotNumber, x, y);
  }

  @Override
  public boolean isItemValid(final ItemStack stack) {
    return RecipeUtils.findRecipe(MeltingRecipe.class, recipe -> recipe.matches(stack)) != null && super.isItemValid(stack);
  }

  @Override
  public int getItemStackLimit(final ItemStack stack) {
    return 1;
  }

  @Override
  public int getSlotStackLimit() {
    return 1;
  }
}
