package lordmonoxide.gradient.containers;

import ic2.core.util.Util;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public abstract class GradientGuiContainer extends GuiContainer {
  protected GradientGuiContainer(final Container container) {
    super(container);
  }
  
  @Override
  public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
    this.drawDefaultBackground();
    super.drawScreen(mouseX, mouseY, partialTicks);
    this.renderToolTips(mouseX, mouseY);
    this.renderHoveredToolTip(mouseX, mouseY);
  }
  
  protected void renderToolTips(final int mouseX, final int mouseY) { }
  
  protected void renderFluidTankToolTip(final FluidTank tank, final int x, final int y) {
    this.drawHoveringText(this.getFluidTankToolTip(tank), x, y, this.fontRenderer);
  }
  
  public List<String> getFluidTankToolTip(final FluidTank tank) {
    final List<String> list = new ArrayList<>();
    
    if(tank.getFluid() != null) {
      list.add(tank.getFluid().getLocalizedName());
    } else {
      list.add(I18n.format(GradientBlocks.BRONZE_BOILER.getUnlocalizedName() + ".fluid_empty"));
    }
    
    list.add(I18n.format(GradientBlocks.BRONZE_BOILER.getUnlocalizedName() + ".fluid_amount", tank.getFluidAmount(), tank.getCapacity()));
    
    return list;
  }
  
  
  public void drawSprite(final double xIn, final double yIn, final double width, final double height, final TextureAtlasSprite sprite, final int color, final double scaleIn) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    final Tessellator tessellator = Tessellator.getInstance();
    final BufferBuilder buffer = tessellator.getBuffer();
    buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
  
    final double x = xIn + this.guiLeft;
    final double y = yIn + this.guiTop;
    final double scale = scaleIn * 16.0D;
  
    final double uS = sprite.getMinU();
    final double vS = sprite.getMinV();
    final double spriteWidth  = sprite.getMaxU() - uS;
    final double spriteHeight = sprite.getMaxV() - vS;
  
    final int a = color >>> 24 & 255;
    final int r = color >>> 16 & 255;
    final int g = color >>> 8 & 255;
    final int b = color & 255;
    
    for(double xS = x; xS <= x + width; xS += scale) {
      final double xE = Math.min(xS + scale, x + width);
      final double uE = uS + (xE - xS) / scale * spriteWidth;
      
      for(double yS = y; yS <= y + height; yS += scale) {
        final double yE = Math.min(yS + scale, y + height);
        final double vE = vS + (yE - yS) / scale * spriteHeight;
        
        buffer.pos(xS, yS, this.zLevel).tex(uS, vS).color(r, g, b, a).endVertex();
        buffer.pos(xS, yE, this.zLevel).tex(uS, vE).color(r, g, b, a).endVertex();
        buffer.pos(xE, yE, this.zLevel).tex(uE, vE).color(r, g, b, a).endVertex();
        buffer.pos(xE, yS, this.zLevel).tex(uE, vS).color(r, g, b, a).endVertex();
      }
    }
    
    tessellator.draw();
  }
  
  protected class ItemButton extends GuiButton {
    public final ItemStack item;
    
    public ItemButton(final int id, final ItemStack item, final int x, final int y) {
      super(id, x + GradientGuiContainer.this.guiLeft, y + GradientGuiContainer.this.guiTop, 20, 20, "");
      this.item = item;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
      super.drawButton(mc, mouseX, mouseY, partialTicks);
      
      if(this.visible) {
        GradientGuiContainer.this.itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 2, this.y + 2);
      }
    }
  }
  
  protected class FluidRenderer {
    private final FluidTank tank;
    public final int x;
    public final int y;
    public final int w;
    public final int h;
    
    public FluidRenderer(final FluidTank tank, final int x, final int y, final int width, final int height) {
      this.tank = tank;
      this.x = x;
      this.y = y;
      this.w = width;
      this.h = height;
    }
    
    public void draw() {
      if(this.tank.getFluid() == null || this.tank.getFluidAmount() == 0) {
        return;
      }
      
      final Fluid fluid = this.tank.getFluid().getFluid();
      final TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill(this.tank.getFluid()).toString());
      final double renderHeight = this.h * Util.limit(this.tank.getFluidAmount() / (double)this.tank.getCapacity(), 0.0d, 1.0d);
      final int color = fluid.getColor(this.tank.getFluid());
      
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GradientGuiContainer.this.drawSprite(this.x, (this.y + this.h) - renderHeight, this.w, renderHeight, sprite, color, 1.0d);
    }
    
    public boolean isMouseOver(final int mouseX, final int mouseY) {
      final int mX = mouseX - GradientGuiContainer.this.guiLeft;
      final int mY = mouseY - GradientGuiContainer.this.guiTop;
      return mX >= this.x && mY >= this.y && mX < this.x + this.w && mY < this.y + this.h;
    }
  }
}
