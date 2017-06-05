package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.GradientContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiFirePit extends GuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/fire_pit.png");
  
  private TileFirePit firePit;
  private InventoryPlayer playerInv;
  
  public GuiFirePit(Container container, TileFirePit firePit, InventoryPlayer playerInv) {
    super(container);
    this.firePit = firePit;
    this.playerInv = playerInv;
  }
  
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    mc.getTextureManager().bindTexture(BG_TEXTURE);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    
    if(this.firePit.isCooking(0)) {
      float percent = this.firePit.getCookingFood(0).cookPercent();
      
      drawTexturedModalRect(x + 122, y + 35, 176, 0, (int)(16 * percent), 14);
    }
  }
  
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for(int slot = 0; slot < TileFirePit.FUEL_SLOTS_COUNT; slot++) {
      if(this.firePit.isBurning(slot)) {
        int x = ContainerFirePit.FUEL_SLOTS_X + (slot % 3) * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        int y = ContainerFirePit.FUEL_SLOTS_Y + (slot / 3) * (GradientContainer.SLOT_Y_SPACING + 8);
        float percent = this.firePit.getBurningFuel(slot).burnPercent();
        
        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }
    
    String name = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".name");
    String fuel = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".fuel");
    String food = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".input");
    
    String heat = I18n.format(GradientBlocks.FIRE_PIT.getUnlocalizedName() + ".heat", (int)this.firePit.getHeat());
    
    fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, 0x404040);
    fontRendererObj.drawString(fuel, ContainerFirePit.FUEL_SLOTS_X, ContainerFirePit.FUEL_SLOTS_Y - fontRendererObj.FONT_HEIGHT - 2, 0x404040);
    fontRendererObj.drawString(food, ContainerFirePit.INPUT_SLOTS_X, ContainerFirePit.INPUT_SLOTS_Y - fontRendererObj.FONT_HEIGHT - 2, 0x404040);
    fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
    
    fontRendererObj.drawString(heat, ContainerFirePit.FUEL_SLOTS_X, 55, 0x404040);
  }
}
