package lordmonoxide.gradient.blocks.bronzefurnace;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBronzeFurnace extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/bronze_furnace.png");
  
  private final TileBronzeFurnace furnace;
  private final InventoryPlayer playerInv;
  private final FluidRenderer metalRenderer;
  
  public GuiBronzeFurnace(final ContainerBronzeFurnace container, final TileBronzeFurnace furnace, final InventoryPlayer playerInv) {
    super(container);
    this.furnace = furnace;
    this.playerInv = playerInv;
    this.metalRenderer = new FluidRenderer(furnace.tankMetal, 148, 19, 12, 47);
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
    final String name = I18n.format(GradientBlocks.BRONZE_FURNACE.getUnlocalizedName() + ".name");
    final String heat = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".heat", (int)this.furnace.getHeat());
    
    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);
    
    this.fontRenderer.drawString(heat, ContainerFirePit.FUEL_SLOTS_X, 58, 0x404040);
  }
  
  @Override
  protected void renderToolTips(final int mouseX, final int mouseY) {
    if(this.metalRenderer.isMouseOver(mouseX, mouseY)) {
      this.renderFluidTankToolTip(this.furnace.tankMetal, mouseX, mouseY);
    }
  }
}
