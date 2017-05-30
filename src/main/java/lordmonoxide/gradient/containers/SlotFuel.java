package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.GradientFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFuel extends SlotItemHandler {
  public SlotFuel(IItemHandler inventory, int slotNumber, int x, int y) {
    super(inventory, slotNumber, x, y);
  }
  
  @Override
  public boolean isItemValid(ItemStack stack) {
    return GradientFuel.instance.has(stack) && super.isItemValid(stack);
  }
  
  @Override
  public int getItemStackLimit(ItemStack stack) {
    return 1;
  }
  
  @Override
  public int getSlotStackLimit() {
    return 1;
  }
}
