package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.containers.ClayCastContainer;
import lordmonoxide.gradient.containers.ClayCrucibleContainer;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.network.PacketSwitchCast;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClayCastScreen extends GradientContainerScreen<ClayCastContainer> {
  public static final ResourceLocation ID = GradientMod.resource("clay_cast");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/clay_cast.png");

  private final ItemClayCastUnhardened held;

  private ButtonItem selectedCast;

  public ClayCastScreen(final ClayCastContainer container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
    this.held = null;
  }

  public ClayCastScreen(final ClayCastContainer container, final PlayerInventory playerInv, final ITextComponent text, final ItemClayCastUnhardened held) {
    super(container, playerInv, text);
    this.held = held;
  }

  @Override
  public void init() {
    this.ySize = 84;
    super.init();

    int x = 12;
    final int y = 33;

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Button button = this.addButtonForTool(cast, x, y);
      x += 22;

      if(this.held.cast == cast) {
        button.enabled = false;
        this.selectedCast = (ButtonItem)button;
      }
    }
  }

  private Button addButtonForTool(final GradientCasts.Cast cast, final int x, final int y) {
    return this.addButton(new ButtonItem(cast.id, GradientItems.castItem(cast, Metals.COPPER, 1), x, y, button -> {
      ClayCastScreen.this.selectedCast.enabled = true;
      button.enabled = false;
      ClayCastScreen.this.selectedCast = (ButtonItem)button;

      PacketSwitchCast.send(GradientCasts.getCast(cast.id));
    }));
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
    final String name = this.selectedCast.item.getDisplayName().getFormattedText();
    this.font.drawString(name, this.xSize / 2 - this.font.getStringWidth(name) / 2, 6, 0x404040);
  }
}
