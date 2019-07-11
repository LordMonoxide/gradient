package lordmonoxide.gradient.inventory;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class GuiInventory3x3Crafting extends InventoryScreen {
  private static final ResourceLocation TEX_BG = GradientMod.resource("textures/gui/inventory.png");

  public GuiInventory3x3Crafting(final PlayerEntity player) {
    super(player);
  }

  @Override
  public void init() {
    super.init();

    for(final Widget button : this.buttons) {
      if(button.id == 10) {
        button.visible = false;
        break;
      }
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    this.minecraft.getTextureManager().bindTexture(TEX_BG);
    this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
  }
}
