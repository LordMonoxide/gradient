package lordmonoxide.gradient.containers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFurnaceInput extends SlotItemHandler {
  public SlotFurnaceInput(final IItemHandler inventory, final int slotNumber, final int x, final int y) {
    super(inventory, slotNumber, x, y);
  }
  
  @Override
  public boolean isItemValid(final ItemStack stack) {
    return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
  }
}