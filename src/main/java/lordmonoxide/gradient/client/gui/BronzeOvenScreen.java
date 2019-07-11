package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.BronzeOvenContainer;
import lordmonoxide.gradient.tileentities.TileBronzeOven;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BronzeOvenScreen extends GradientContainerScreen<BronzeOvenContainer> {
  public static final ResourceLocation ID = GradientMod.resource("bronze_oven");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/bronze_oven.png");

  private final TileBronzeOven oven;
  private final PlayerInventory playerInv;
  private final FluidRenderer steamRenderer;

  public BronzeOvenScreen(final BronzeOvenContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.oven = container.oven;
    this.playerInv = playerInv;
    this.steamRenderer = new FluidRenderer(this.oven.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.blit(x, y, 0, 0, this.xSize, this.ySize);

    if(this.oven.isCooking()) {
      this.blit(x + 32, y + 35, 192, 0, (int)(16.0f * this.oven.getCookPercent()), 14);
    }

    this.steamRenderer.draw();

    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    this.blit(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_OVEN.getTranslationKey() + ".name");

    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
    this.font.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.steamRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.oven.tankSteam, mouseX, mouseY);
    }
  }
}
