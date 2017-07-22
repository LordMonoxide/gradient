package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.containers.GradientContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiClayCrucible extends GuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/clay_crucible.png");
  
  private TileClayCrucible te;
  private InventoryPlayer playerInv;
  
  public GuiClayCrucible(ContainerClayCrucible container, TileClayCrucible te, InventoryPlayer playerInv) {
    super(container);
    this.te = te;
    this.playerInv = playerInv;
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    int x = (this.width  - this.xSize) / 2;
    int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for(int slot = 0; slot < TileClayCrucible.METAL_SLOTS_COUNT; slot++) {
      if(this.te.isMelting(slot)) {
        int x = ContainerClayCrucible.METAL_SLOTS_X + (slot % 5) * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        int y = ContainerClayCrucible.METAL_SLOTS_Y + (slot / 5) * (GradientContainer.SLOT_Y_SPACING + 2);
        float percent = this.te.getMeltingMetal(slot).meltPercent();
        
        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }
    
    int x = ContainerClayCrucible.METAL_SLOTS_X + (GradientContainer.SLOT_X_SPACING + 8) * 5;
    int y = ContainerClayCrucible.METAL_SLOTS_Y;
    
    for(TileClayCrucible.MoltenMetal molten : this.te.getMoltenMetals()) {
      //TODO: lang for metal name
      String text = I18n.format(GradientBlocks.CLAY_CRUCIBLE.getUnlocalizedName() + ".molten", molten.metal.name, molten.amount());
      this.fontRenderer.drawString(text, x, y, 0x404040);
      y += this.fontRenderer.FONT_HEIGHT;
    }
    
    String name = I18n.format(GradientBlocks.CLAY_CRUCIBLE.getUnlocalizedName() + ".name");
    String heat = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".heat", (int)this.te.getHeat());
    
    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);
    
    this.fontRenderer.drawString(heat, ContainerFirePit.FUEL_SLOTS_X, 58, 0x404040);
  }
}
