package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.ClayCrucibleContainer;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClayCrucibleScreen extends GradientContainerScreen<ClayCrucibleContainer> {
  public static final ResourceLocation ID = GradientMod.resource("clay_crucible");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/clay_crucible.png");

  private final TileClayCrucible te;
  private final PlayerInventory playerInv;
  private final FluidRenderer metalRenderer;

  public ClayCrucibleScreen(final ClayCrucibleContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.te = container.crucible;
    this.playerInv = playerInv;
    this.metalRenderer = new FluidRenderer(this.te.tank, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.blit(x, y, 0, 0, this.xSize, this.ySize);

    this.metalRenderer.draw();

    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    this.blit(x + this.metalRenderer.x, y + this.metalRenderer.y, 177, 0, this.metalRenderer.w, this.metalRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    for(int slot = 0; slot < TileClayCrucible.METAL_SLOTS_COUNT; slot++) {
      if(this.te.isMelting(slot)) {
        final int x = ClayCrucibleContainer.METAL_SLOTS_X + (slot % 5) * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        final int y = ClayCrucibleContainer.METAL_SLOTS_Y + (slot / 5) * (GradientContainer.SLOT_Y_SPACING + 2);
        final float percent = this.te.getMeltingMetal(slot).meltPercent();

        fill(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }

    final String name = I18n.format(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getTranslationKey() + ".name");
    final String heat = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".heat", (int)this.te.getHeat());

    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
    this.font.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);

    this.font.drawString(heat, ClayCrucibleContainer.FUEL_SLOTS_X, 58, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.metalRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.te.tank, mouseX, mouseY);
    }
  }
}
