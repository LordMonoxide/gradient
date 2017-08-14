package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.CastItem;
import lordmonoxide.gradient.GradientCasts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiClayCast extends GuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/clay_cast.png");
  
  private final ItemStack held;
  
  private ItemButton selectedCast;
  
  public GuiClayCast(final Container container, final ItemStack held) {
    super(container);
    this.held = held;
  }
  
  @Override
  public void initGui() {
    this.ySize = 84;
    super.initGui();
    
    int x = 12;
    int y = 33;
    
    for(final GradientCasts.Cast cast : GradientCasts.CASTS) {
      final GuiButton button = this.addButtonForTool(cast, x, y);
      x += 22;
      
      if(this.held.getMetadata() == cast.id) {
        button.enabled = false;
        this.selectedCast = (ItemButton)button;
      }
    }
  }
  
  private GuiButton addButtonForTool(final GradientCasts.Cast cast, final int x, final int y) {
    return this.addButton(new ItemButton(cast.id, cast.itemOverride != null ? cast.itemOverride.getDefaultInstance() : CastItem.getCastItem(cast, GradientMetals.INVALID_METAL), x + this.guiLeft, y + this.guiTop));
  }
  
  @Override
  protected void actionPerformed(final GuiButton button) throws IOException {
    if(button instanceof ItemButton) {
      final ItemButton cast = (ItemButton)button;
      this.selectedCast.enabled = true;
      cast.enabled = false;
      this.selectedCast = cast;
      
      PacketSwitchCast.send(GradientCasts.CASTS.get(cast.id));
    }
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    final String name = this.selectedCast.item.getDisplayName();
    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
  }
  
  protected class ItemButton extends GuiButton {
    public final ItemStack item;
    
    public ItemButton(final int id, final ItemStack item, final int x, final int y) {
      super(id, x, y, 20, 20, "");
      this.item = item;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
      super.drawButton(mc, mouseX, mouseY);
      
      if(this.visible) {
        GuiClayCast.this.itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 2, this.y + 2);
      }
    }
  }
}
