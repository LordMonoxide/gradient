package lordmonoxide.gradient.blocks.bronzegrinder;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBronzeGrinder extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/bronze_grinder.png");

  private final TileBronzeGrinder grinder;
  private final InventoryPlayer playerInv;
  private final FluidRenderer steamRenderer;

  public GuiBronzeGrinder(final ContainerBronzeGrinder container, final TileBronzeGrinder grinder, final InventoryPlayer playerInv) {
    super(container);
    this.grinder = grinder;
    this.playerInv = playerInv;
    this.steamRenderer = new FluidRenderer(grinder.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    if(this.grinder.isWorking()) {
      this.drawTexturedModalRect(x + 32, y + 35, 192, 0, (int)(16.0f * this.grinder.getWorkPercent()), 14);
    }

    this.steamRenderer.draw();

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_GRINDER.getTranslationKey() + ".name");

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.steamRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.grinder.tankSteam, mouseX, mouseY);
    }
  }
}
