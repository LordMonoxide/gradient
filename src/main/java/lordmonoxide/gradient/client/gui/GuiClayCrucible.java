package lordmonoxide.gradient.client.gui;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.ContainerClayCrucible;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiClayCrucible extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/clay_crucible.png");

  private final TileClayCrucible te;
  private final InventoryPlayer playerInv;
  private final FluidRenderer metalRenderer;

  public GuiClayCrucible(final ContainerClayCrucible container, final TileClayCrucible te, final InventoryPlayer playerInv) {
    super(container);
    this.te = te;
    this.playerInv = playerInv;
    this.metalRenderer = new FluidRenderer(te.tank, 148, 19, 12, 47);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    this.metalRenderer.draw();

    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    this.drawTexturedModalRect(x + this.metalRenderer.x, y + this.metalRenderer.y, 177, 0, this.metalRenderer.w, this.metalRenderer.h);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    for(int slot = 0; slot < TileClayCrucible.METAL_SLOTS_COUNT; slot++) {
      if(this.te.isMelting(slot)) {
        final int x = ContainerClayCrucible.METAL_SLOTS_X + (slot % 5) * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        final int y = ContainerClayCrucible.METAL_SLOTS_Y + (slot / 5) * (GradientContainer.SLOT_Y_SPACING + 2);
        final float percent = this.te.getMeltingMetal(slot).meltPercent();

        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }

    final String name = I18n.format(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getTranslationKey() + ".name");
    final String heat = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".heat", (int)this.te.getHeat());

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);

    this.fontRenderer.drawString(heat, ContainerClayCrucible.FUEL_SLOTS_X, 58, 0x404040);
  }

  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.metalRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.te.tank, mouseX, mouseY);
    }
  }
}
