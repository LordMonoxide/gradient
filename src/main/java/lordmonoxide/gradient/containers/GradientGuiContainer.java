package lordmonoxide.gradient.containers;

import ic2.core.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public abstract class GradientGuiContainer extends GuiContainer {
  protected GradientGuiContainer(final Container container) {
    super(container);
  }
  
  public void drawSprite(final double xIn, final double yIn, final double width, final double height, final TextureAtlasSprite sprite, final int color, final double scaleIn) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder buffer = tessellator.getBuffer();
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
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, float partialTicks) {
      super.drawButton(mc, mouseX, mouseY, partialTicks);
      
      if(this.visible) {
        GradientGuiContainer.this.itemRender.renderItemAndEffectIntoGUI(this.item, this.x + 2, this.y + 2);
      }
    }
  }
  
  protected class FluidRenderer {
    public FluidRenderer() { }
    
    public void renderFluid(FluidTank tank, double x, double y, double width, double height) {
      if(tank.getFluid() == null || tank.getFluidAmount() == 0) {
        return;
      }
      
      final Fluid fluid = tank.getFluid().getFluid();
      final TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill(tank.getFluid()).toString());
      final double renderHeight = height * Util.limit(tank.getFluidAmount() / (double)tank.getCapacity(), 0.0d, 1.0d);
      final int color = fluid.getColor(tank.getFluid());
      
      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GradientGuiContainer.this.drawSprite(x, (y + height) - renderHeight, width, renderHeight, sprite, color, 1.0d);
    }
  }
}
