package lordmonoxide.gradient.progression;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progression.components.JournalComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiJournalEntry extends GuiScreen {
  private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation(GradientMod.MODID,"textures/gui/journal.png");

  private static final int FRAME_WIDTH  = 282;
  private static final int FRAME_HEIGHT = 180;

  private final JournalEntry entry;

  private final GuiScreen previousGui;

  public GuiJournalEntry(final JournalEntry entry, final GuiScreen previousGui) {
    this.entry = entry;
    this.previousGui = previousGui;
  }

  public FontRenderer getFontRenderer() {
    return this.fontRenderer;
  }

  public RenderItem getItemRenderer() {
    return this.itemRender;
  }

  protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    if(keyCode == 1) {
      this.mc.displayGuiScreen(this.previousGui);

      if(this.mc.currentScreen == null) {
        this.mc.setIngameFocus();
      }

      return;
    }

    super.keyTyped(typedChar, keyCode);
  }

  public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    this.drawDefaultBackground();
    this.drawJournal(mouseX, mouseY, partialTicks);
  }

  protected void drawJournal(final int mouseX, final int mouseY, final float partialTicks) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);

    GlStateManager.pushMatrix();
    GlStateManager.translate((this.width - FRAME_WIDTH) / 2, 3, 0);

    Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, 512, 256);

    GlStateManager.pushMatrix();
    GlStateManager.translate(16, 16, 0);

    GlStateManager.pushMatrix();

    int page = 0;
    int pageY = 0;

    for(final JournalComponent component : this.entry.components) {
      final int componentHeight = component.getRenderedHeight(this);

      if(pageY + componentHeight > 146) {
        page++;
        pageY = 0;

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        if((page & 1) == 1) {
          GlStateManager.translate(136, 0, 0);
        }
      }

      component.render(this);

      GlStateManager.translate(0, componentHeight, 0);

      pageY += componentHeight;
    }

    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
  }
}
