package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.BronzeBoilerContainer;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BronzeBoilerScreen extends GradientContainerScreen<BronzeBoilerContainer> {
  public static final ResourceLocation ID = GradientMod.resource("bronze_boiler");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/bronze_boiler.png");

  private final TileBronzeBoiler boiler;
  private final PlayerInventory playerInv;
  private final FluidRenderer waterRenderer;
  private final FluidRenderer steamRenderer;

  public BronzeBoilerScreen(final BronzeBoilerContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.boiler = container.boiler;
    this.playerInv = playerInv;
    this.waterRenderer = new FluidRenderer(this.boiler.tankWater, 124, 19, 12, 47);
    this.steamRenderer = new FluidRenderer(this.boiler.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.blit(x, y, 0, 0, this.xSize, this.ySize);

    this.waterRenderer.draw();
    this.steamRenderer.draw();

    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    this.blit(x + this.waterRenderer.x, y + this.waterRenderer.y, 177, 0, this.waterRenderer.w, this.waterRenderer.h);
    this.blit(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".name");

    final String heat = I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".heat", (int)this.boiler.getHeat());

    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
    this.font.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);

    this.font.drawString(heat, 13, 55, 0x404040);
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
