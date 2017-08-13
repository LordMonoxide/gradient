package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.GradientFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFuel extends SlotItemHandler {
  public SlotFuel(final IItemHandler inventory, final int slotNumber, final int x, final int y) {
    super(inventory, slotNumber, x, y);
  }
  
  @Override
  public boolean isItemValid(final ItemStack stack) {
    return GradientFuel.has(stack) && super.isItemValid(stack);
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
