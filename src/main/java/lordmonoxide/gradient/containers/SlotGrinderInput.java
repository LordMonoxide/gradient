package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.recipes.GrinderRecipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotGrinderInput extends SlotItemHandler {
  public SlotGrinderInput(final IItemHandler inventory, final int slotNumber, final int x, final int y) {
    super(inventory, slotNumber, x, y);
  }

  @Override
  public boolean isItemValid(final ItemStack stack) {
    return !GrinderRecipes.getOutput(stack).isEmpty();
  }
}
