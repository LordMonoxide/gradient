package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class JournalGather extends JournalComponent {
  private static final int ITEM_SIZE = 18;
  
  private final EntityPlayer player;
  private final ItemStack[] stacks;
  
  public JournalGather(String id, EntityPlayer player, ItemStack... stacks) {
    super(id);
    this.player = player;
    this.stacks = stacks;
  }
  
  @Override
  public void render(GuiJournalEntry screen) {
    int y = 0;
    
    for(ItemStack stack : this.stacks) {
      screen.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, y);
      
      screen.getFontRenderer().drawString(this.hasItem(stack) + "/" + stack.getCount(), ITEM_SIZE + 2, y + ((ITEM_SIZE - 2) - screen.getFontRenderer().FONT_HEIGHT) / 2 + 2, 0x0);
      
      y += ITEM_SIZE;
    }
  }
  
  private int hasItem(ItemStack in) {
    int out = 0;
    
    for(ItemStack stack : this.player.inventory.mainInventory) {
      if(stack.isItemEqual(in)) {
        out += stack.getCount();
      }
    }
    
    return out;
  }
  
  @Override
  public int getRenderedHeight(GuiJournalEntry gui) {
    return this.stacks.length * ITEM_SIZE - 2;
  }
  
  @Override
  public boolean isCompletable() {
    for(ItemStack stack : this.stacks) {
      if(this.hasItem(stack) < stack.getCount()) {
        return false;
      }
    }
    
    return true;
  }
}
