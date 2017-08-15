package lordmonoxide.gradient.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GradientGuiContainer extends GuiContainer {
  protected GradientGuiContainer(Container container) {
    super(container);
  }
  
  protected class ItemButton extends GuiButton {
    public final ItemStack item;
    
    public ItemButton(final int id, final ItemStack item, final int x, final int y) {
      super(id, x + GradientGuiContainer.this.guiLeft, y + GradientGuiContainer.this.guiTop, 20, 20, "");
      this.item = item;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
      super.drawButton(mc, mouseX, mouseY);
      
      if(this.visible) {
        GradientGuiContainer.this.itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 2, this.y + 2);
      }
    }
  }
}
