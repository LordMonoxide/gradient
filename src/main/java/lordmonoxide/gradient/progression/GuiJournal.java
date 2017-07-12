package lordmonoxide.gradient.progression;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiJournal extends GuiScreen {
  private static final ResourceLocation BACKGROUND = new ResourceLocation(GradientMod.MODID,"textures/gui/journal_background.png");
  
  private static final int FRAME_WIDTH  = 256;
  private static final int FRAME_HEIGHT = 202;
  private static final int INNER_WIDTH  = 224;
  private static final int INNER_HEIGHT = 153;
  private static final int CONTENT_SIZE = 288;
  private static final int TEXEL_SIZE   = 16;
  private static final int CELL_SIZE    = 26;
  
  private int mouseDownX;
  private int mouseDownY;
  
  private int scrollX = INNER_WIDTH / 2;
  private int scrollY = INNER_HEIGHT / 2;
  
  private int frameX;
  private int frameY;
  
  private JournalEntry clickedEntry;
  
  @Override
  public void initGui() {
    this.frameX = (this.width  - FRAME_WIDTH)  / 2;
    this.frameY = (this.height - FRAME_HEIGHT) / 2;
    
    for(JournalEntry entry : Journal.instance.entries) {
      JournalButton button = new JournalButton(0, entry);
      button.enabled = entry.isAvailable();
      
      this.addButton(button);
    }
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawDefaultBackground();
    this.drawJournal(mouseX, mouseY, partialTicks);
  }
  
  protected void drawJournal(int mouseX, int mouseY, float partialTicks) {
    GlStateManager.enableTexture2D();
    GlStateManager.disableLighting();
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableColorMaterial();
    GlStateManager.disableDepth();
    
    GlStateManager.pushMatrix();
    GlStateManager.translate(this.frameX + TEXEL_SIZE + this.scrollX, this.frameY + (TEXEL_SIZE + 1) + this.scrollY, -200.0F);
    GlStateManager.scale(1.0F, 1.0F, 1.0F);
    
    int baseColour = CONTENT_SIZE >> 4;
    int texelCount = CONTENT_SIZE % TEXEL_SIZE;
    
    int maxX = (-this.scrollX / TEXEL_SIZE);
    int maxY = (-this.scrollY / TEXEL_SIZE);
    
    for(int y = maxY - (this.scrollY > 0 ? 1 : 0); y - texelCount < INNER_HEIGHT / TEXEL_SIZE + 1 + maxY + (this.scrollY < 0 ? 1 : 0); ++y) {
      float colour = 0.6F - (baseColour + y) / 25.0F * 0.3F;
      GlStateManager.color(colour, colour, colour, 1.0F);
      
      for(int x = maxX - (this.scrollX > 0 ? 1 : 0); x - texelCount < INNER_WIDTH / TEXEL_SIZE + maxX + (this.scrollX < 0 ? 1 : 0); ++x) {
        TextureAtlasSprite textureatlassprite = this.getTexture(Blocks.DIRT);
        
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.drawTexturedModalRect(x * TEXEL_SIZE - texelCount, y * TEXEL_SIZE - texelCount, textureatlassprite, TEXEL_SIZE, TEXEL_SIZE);
      }
    }
    
    for(GuiButton button : this.buttonList) {
      button.drawButton(this.mc, mouseX, mouseY);
    }
    
    GlStateManager.popMatrix();
    
    GlStateManager.enableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BACKGROUND);
    this.drawTexturedModalRect(this.frameX, this.frameY, 0, 0, FRAME_WIDTH, FRAME_HEIGHT);
    
    //super.drawScreen(mouseX, mouseY, partialTicks);
    
    GlStateManager.enableDepth();
    GlStateManager.enableLighting();
    RenderHelper.disableStandardItemLighting();
    
    for(GuiButton button : this.buttonList) {
      if(button.isMouseOver()) {
        ((JournalButton)button).drawText(mouseX, mouseY);
      }
    }
  }
  
  private TextureAtlasSprite getTexture(Block blockIn) {
    return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(blockIn.getDefaultState());
  }
  
  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    
    this.mouseDownX = mouseX;
    this.mouseDownY = mouseY;
  }
  
  @Override
  protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    
    this.scrollX += (mouseX - this.mouseDownX);
    this.scrollY += (mouseY - this.mouseDownY);
    
    this.mouseDownX = mouseX;
    this.mouseDownY = mouseY;
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    
    if(this.clickedEntry != null) {
      this.mc.displayGuiScreen(new GuiJournalEntry(this.clickedEntry, this));
      this.clickedEntry = null;
    }
  }
  
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    this.clickedEntry = ((JournalButton)button).entry;
  }
  
  protected class JournalButton extends GuiButton {
    private final String name;
    private final ItemStack icon;
    private final JournalEntry entry;
    
    public JournalButton(int id, JournalEntry entry) {
      super(id, entry.x * CELL_SIZE - entry.type.textureW / 2, entry.y * CELL_SIZE - entry.type.textureH / 2, entry.type.textureW, entry.type.textureH, "");
      
      this.name  = entry.name;
      this.icon  = entry.icon;
      this.entry = entry;
    }
    
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(this.visible) {
        this.hovered = (mouseX - this.scaledX()) >= this.xPosition && (mouseY - this.scaledY()) >= this.yPosition && (mouseX - this.scaledX()) < this.xPosition + this.width && (mouseY - this.scaledY()) < this.yPosition + this.height;
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, this.entry.type.textureX, this.entry.type.textureY + this.getHoverState(this.hovered) * this.height, this.width, this.height);
        
        GuiJournal.this.itemRender.renderItemAndEffectIntoGUI(this.icon,this.xPosition + (this.width - TEXEL_SIZE) / 2, this.yPosition + (this.height - TEXEL_SIZE) / 2);
        
        this.mouseDragged(mc, mouseX, mouseY);
      }
    }
    
    public void drawText(int mouseX, int mouseY) {
      GuiJournal.this.drawHoveringText(I18n.format("journal." + this.name), mouseX, mouseY);
    }
    
    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      return super.mousePressed(mc, mouseX - this.scaledX(), mouseY - this.scaledY());
    }
    
    private int scaledX() {
      return GuiJournal.this.frameX + GuiJournal.this.scrollX + TEXEL_SIZE;
    }
    
    private int scaledY() {
      return GuiJournal.this.frameY + GuiJournal.this.scrollY + TEXEL_SIZE + 1;
    }
  }
}
