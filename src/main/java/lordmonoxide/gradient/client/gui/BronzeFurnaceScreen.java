package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.BronzeFurnaceContainer;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.network.PacketLightBronzeFurnace;
import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BronzeFurnaceScreen extends GradientContainerScreen<BronzeFurnaceContainer> {
  public static final ResourceLocation ID = GradientMod.resource("bronze_furnace");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/bronze_furnace.png");

  private final TileBronzeFurnace furnace;
  private final PlayerInventory playerInv;

  public BronzeFurnaceScreen(final BronzeFurnaceContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.furnace = container.furnace;
    this.playerInv = playerInv;
  }

  @Override
  public void init() {
    super.init();
    this.addButton(new ButtonItem(0, Items.FLINT_AND_STEEL.getDefaultInstance(), 92, 22, button -> PacketLightBronzeFurnace.send(BronzeFurnaceScreen.this.furnace.getPos())));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color4f(1, 1, 1, 1);
    this.minecraft.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.blit(x, y, 0, 0, this.xSize, this.ySize);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    for(int slot = 0; slot < TileBronzeFurnace.FUEL_SLOTS_COUNT; slot++) {
      if(this.furnace.isBurning(slot)) {
        final int x = BronzeFurnaceContainer.FUEL_SLOTS_X + slot % 3 * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        final int y = BronzeFurnaceContainer.FUEL_SLOTS_Y + slot / 3 * (GradientContainer.SLOT_Y_SPACING + 2);
        final float percent = this.furnace.getBurningFuel(slot).burnPercent();

        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }

    final String name = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".name");
    final String fuel = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".fuel");

    final String heat = I18n.format(GradientBlocks.BRONZE_FURNACE.getTranslationKey() + ".heat", (int)this.furnace.getHeat());

    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
    this.font.drawString(fuel, BronzeFurnaceContainer.FUEL_SLOTS_X, BronzeFurnaceContainer.FUEL_SLOTS_Y - this.font.FONT_HEIGHT - 2, 0x404040);
    this.font.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 8, this.ySize - 94, 0x404040);

    this.font.drawString(heat, 92, 47, 0x404040);
  }
}
