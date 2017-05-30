package lordmonoxide.gradient.containers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotOutput extends SlotItemHandler {
  public SlotOutput(IItemHandler inventory, int slotNumber, int x, int y) {
    super(inventory, slotNumber, x, y);
  }
  
  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }
}
