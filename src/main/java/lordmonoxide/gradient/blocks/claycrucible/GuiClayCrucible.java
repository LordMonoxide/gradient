package lordmonoxide.gradient.blocks.claycrucible;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiClayCrucible extends GuiContainer {
  public GuiClayCrucible(Container container, TileClayCrucible firePit, InventoryPlayer playerInv) {
    super(container);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    
  }
}
