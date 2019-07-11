package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.BronzeGrinderContainer;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BronzeGrinderScreen extends GradientContainerScreen<BronzeGrinderContainer> {
  public static final ResourceLocation ID = GradientMod.resource("bronze_grinder");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/bronze_grinder.png");

  private final TileBronzeGrinder grinder;
  private final PlayerInventory playerInv;
  private final FluidRenderer steamRenderer;

  public BronzeGrinderScreen(final BronzeGrinderContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.grinder = container.grinder;
    this.playerInv = playerInv;
    this.steamRenderer = new FluidRenderer(this.grinder.tankSteam, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.blit(x, y, 0, 0, this.xSize, this.ySize);

    if(this.grinder.isWorking()) {
      this.blit(x + 32, y + 35, 192, 0, (int)(16.0f * this.grinder.getWorkPercent()), 14);
    }

    this.steamRenderer.draw();

    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    this.blit(x + this.steamRenderer.x, y + this.steamRenderer.y, 177, 0, this.steamRenderer.w, this.steamRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = I18n.format(GradientBlocks.BRONZE_GRINDER.getTranslationKey() + ".name");

    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
    this.font.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.steamRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.grinder.tankSteam, mouseX, mouseY);
    }
  }
}
