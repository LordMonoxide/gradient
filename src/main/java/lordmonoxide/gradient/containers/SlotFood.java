package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.GradientFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFood extends SlotItemHandler {
  public SlotFood(IItemHandler inventory, int slotNumber, int x, int y) {
    super(inventory, slotNumber, x, y);
  }
  
  @Override
  public boolean isItemValid(ItemStack stack) {
    return GradientFood.instance.has(stack) && super.isItemValid(stack);
  }
}