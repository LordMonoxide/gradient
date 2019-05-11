package lordmonoxide.gradient.client.gui;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.network.PacketSwitchCast;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiClayCast extends GradientGuiContainer {
  public static final ResourceLocation ID = GradientMod.resource("clay_cast");

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MOD_ID, "textures/gui/clay_cast.png");

  private final ItemClayCastUnhardened held;

  private ItemButton selectedCast;

  public GuiClayCast(final Container container, final ItemClayCastUnhardened held) {
    super(container);
    this.held = held;
  }

  @Override
  public void initGui() {
    this.ySize = 84;
    super.initGui();

    int x = 12;
    final int y = 33;

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final GuiButton button = this.addButtonForTool(cast, x, y);
      x += 22;

      if(this.held.cast == cast) {
        button.enabled = false;
        this.selectedCast = (ItemButton)button;
      }
    }
  }

  private GuiButton addButtonForTool(final GradientCasts.Cast cast, final int x, final int y) {
    return this.addButton(new ItemButton(cast.id, GradientItems.castItem(cast, Metals.COPPER, 1), x, y) {
      @Override
      public void onClick(final double mouseX, final double mouseY) {
        GuiClayCast.this.selectedCast.enabled = true;
        this.enabled = false;
        GuiClayCast.this.selectedCast = this;

        PacketSwitchCast.send(GradientCasts.getCast(cast.id));
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
    final String name = this.selectedCast.item.getDisplayName().getFormattedText();
    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
  }
}
