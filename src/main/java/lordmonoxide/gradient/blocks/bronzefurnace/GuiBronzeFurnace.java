package lordmonoxide.gradient.blocks.bronzefurnace;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBronzeFurnace extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/bronze_furnace.png");

  private final TileBronzeFurnace furnace;
  private final InventoryPlayer playerInv;
  private final FluidRenderer steamRenderer;

  public GuiBronzeFurnace(final ContainerBronzeFurnace container, final TileBronzeFurnace furnace, final InventoryPlayer playerInv) {
    super(container);
    this.furnace = furnace;
    this.playerInv = playerInv;
    this.steamRenderer = new FluidRenderer(furnace.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    if(this.furnace.isCooking()) {
      this.drawTexturedModalRect(x + 32, y + 35, 192, 0, (int)(16.0f * this.furnace.getCookPercent()), 14);
    }

    this.steamRenderer.draw();

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".name");

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.steamRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.furnace.tankSteam, mouseX, mouseY);
    }
  }
}
