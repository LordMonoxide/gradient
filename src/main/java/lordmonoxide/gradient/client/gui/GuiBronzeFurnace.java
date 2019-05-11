package lordmonoxide.gradient.client.gui;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.ContainerBronzeFurnace;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import lordmonoxide.gradient.network.PacketLightBronzeFurnace;
import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiBronzeFurnace extends GradientGuiContainer {
  public static final ResourceLocation ID = GradientMod.resource("bronze_furnace");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/bronze_furnace.png");

  private final TileBronzeFurnace furnace;
  private final InventoryPlayer playerInv;

  public GuiBronzeFurnace(final ContainerBronzeFurnace container) {
    super(container);
    this.furnace = container.furnace;
    this.playerInv = container.playerInv;
  }

  @Override
  public void initGui() {
    super.initGui();

    this.addButton(new ItemButton(0, Items.FLINT_AND_STEEL.getDefaultInstance(), 92, 22) {
      @Override
      public void onClick(final double mouseX, final double mouseY) {
        PacketLightBronzeFurnace.send(GuiBronzeFurnace.this.furnace.getPos());
      }
    });
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    for(int slot = 0; slot < TileBronzeFurnace.FUEL_SLOTS_COUNT; slot++) {
      if(this.furnace.isBurning(slot)) {
        final int x = ContainerBronzeFurnace.FUEL_SLOTS_X + slot % 3 * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        final int y = ContainerBronzeFurnace.FUEL_SLOTS_Y + slot / 3 * (GradientContainer.SLOT_Y_SPACING + 2);
        final float percent = this.furnace.getBurningFuel(slot).burnPercent();

        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }

    final String name = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".name");
    final String fuel = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".fuel");

    final String heat = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".heat", (int)this.furnace.getHeat());

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(fuel, ContainerBronzeFurnace.FUEL_SLOTS_X, ContainerBronzeFurnace.FUEL_SLOTS_Y - this.fontRenderer.FONT_HEIGHT - 2, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);

    this.fontRenderer.drawString(heat, 92, 47, 0x404040);
  }
}
