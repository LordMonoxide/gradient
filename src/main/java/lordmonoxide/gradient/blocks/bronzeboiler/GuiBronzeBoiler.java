package lordmonoxide.gradient.blocks.bronzeboiler;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBronzeBoiler extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/bronze_boiler.png");

  private final TileBronzeBoiler boiler;
  private final InventoryPlayer playerInv;
  private final FluidRenderer waterRenderer;
  private final FluidRenderer steamRenderer;

  public GuiBronzeBoiler(final ContainerBronzeBoiler container, final TileBronzeBoiler boiler, final InventoryPlayer playerInv) {
    super(container);
    this.boiler = boiler;
    this.playerInv = playerInv;
    this.waterRenderer = new FluidRenderer(boiler.tankWater, 124, 19, 12, 47);
    this.steamRenderer = new FluidRenderer(boiler.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    this.waterRenderer.draw();
    this.steamRenderer.draw();

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(x + this.waterRenderer.x, y + this.waterRenderer.y, 177, 0, this.waterRenderer.w, this.waterRenderer.h);
    this.drawTexturedModalRect(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".name");

    final String heat = I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".heat", (int)this.boiler.getHeat());

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);

    this.fontRenderer.drawString(heat, 13, 55, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.waterRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.boiler.tankWater, mouseX, mouseY);
    }

    if(this.steamRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.boiler.tankSteam, mouseX, mouseY);
    }
  }
}
