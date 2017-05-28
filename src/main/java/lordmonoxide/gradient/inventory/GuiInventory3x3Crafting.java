package lordmonoxide.gradient.inventory;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInventory3x3Crafting extends GuiInventory {
  public GuiInventory3x3Crafting(EntityPlayer p) {
    super(p);
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    this.mc.getTextureManager().bindTexture(new ResourceLocation(GradientMod.MODID, "textures/gui/inventory.png")); //$NON-NLS-1$
    
    drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
  }
}
