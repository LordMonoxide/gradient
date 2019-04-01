package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.recipes.FuelRecipe;
import lordmonoxide.gradient.recipes.RecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFuel extends SlotItemHandler {
  public SlotFuel(final IItemHandler inventory, final int slotNumber, final int x, final int y) {
    super(inventory, slotNumber, x, y);
  }

  @Override
  public boolean isItemValid(final ItemStack stack) {
    return RecipeHelper.findRecipe(FuelRecipe.class, r -> r.matches(stack)) != null && super.isItemValid(stack);
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
