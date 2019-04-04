package lordmonoxide.gradient.inventory;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiInventory3x3Crafting extends GuiInventory {
  public GuiInventory3x3Crafting(final EntityPlayer player) {
    super(player);
  }

  @Override
  public void initGui() {
    super.initGui();

    final GuiButton guideBookButton = this.buttons.get(10);
    guideBookButton.x = this.guiLeft + 152;
    guideBookButton.y = this.guiTop  +  47;

    this.buttons.set(10, new GuideBookButton(guideBookButton));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    this.mc.getTextureManager().bindTexture(new ResourceLocation(GradientMod.MODID, "textures/gui/inventory.png")); //$NON-NLS-1$

    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
  }

  public class GuideBookButton extends GuiButton {
    private final GuiButton wrapped;

    public GuideBookButton(final GuiButton wrapped) {
      super(wrapped.id, wrapped.x, wrapped.y, wrapped.width, wrapped.height, wrapped.displayString);
      this.wrapped = wrapped;
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
      this.wrapped.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClick(final double mouseX, final double mouseY) {
      this.wrapped.onClick(mouseX, mouseY);
      this.wrapped.x = GuiInventory3x3Crafting.this.guiLeft + 152;
      this.wrapped.y = GuiInventory3x3Crafting.this.guiTop  +  47;
    }

    @Override
    public void onRelease(final double mouseX, final double mouseY) {
      this.wrapped.onRelease(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(final double p_mouseClicked_1_, final double p_mouseClicked_3_, final int p_mouseClicked_5_) {
      return this.wrapped.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public boolean mouseReleased(final double p_mouseReleased_1_, final double p_mouseReleased_3_, final int p_mouseReleased_5_) {
      return this.wrapped.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseDragged(final double p_mouseDragged_1_, final double p_mouseDragged_3_, final int p_mouseDragged_5_, final double p_mouseDragged_6_, final double p_mouseDragged_8_) {
      return this.wrapped.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean isMouseOver() {
      return this.wrapped.isMouseOver();
    }

    @Override
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
      this.wrapped.drawButtonForegroundLayer(mouseX, mouseY);
    }

    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {
      this.wrapped.playPressSound(soundHandlerIn);
    }

    @Override
    public int getWidth() {
      return this.wrapped.getWidth();
    }

    @Override
    public void setWidth(final int width) {
      this.wrapped.setWidth(width);
    }

    @Override
    public void drawCenteredString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
      this.wrapped.drawCenteredString(fontRendererIn, text, x, y, color);
    }

    @Override
    public void drawString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
      this.wrapped.drawString(fontRendererIn, text, x, y, color);
    }

    @Override
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
      this.wrapped.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    @Override
    public void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
      this.wrapped.drawTexturedModalRect(xCoord, yCoord, minU, minV, maxU, maxV);
    }

    @Override
    public void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
      this.wrapped.drawTexturedModalRect(xCoord, yCoord, textureSprite, widthIn, heightIn);
    }

    @Override
    public boolean mouseScrolled(final double p_mouseScrolled_1_) {
      return this.wrapped.mouseScrolled(p_mouseScrolled_1_);
    }

    @Override
    public boolean keyPressed(final int p_keyPressed_1_, final int p_keyPressed_2_, final int p_keyPressed_3_) {
      return this.wrapped.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    public boolean keyReleased(final int p_keyReleased_1_, final int p_keyReleased_2_, final int p_keyReleased_3_) {
      return this.wrapped.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
    }

    @Override
    public boolean charTyped(final char p_charTyped_1_, final int p_charTyped_2_) {
      return this.wrapped.charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    public void focusChanged(final boolean focused) {
      this.wrapped.focusChanged(focused);
    }

    @Override
    public boolean canFocus() {
      return this.wrapped.canFocus();
    }
  }
}
