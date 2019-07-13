package lordmonoxide.gradient.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.containers.GradientContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class GradientContainerScreen<T extends GradientContainer> extends ContainerScreen<T> {
  protected GradientContainerScreen(final T container, final PlayerInventory playerInv, final ITextComponent text) {
    super(container, playerInv, text);
  }

  @Override
  public void render(final int mouseX, final int mouseY, final float partialTicks) {
    super.render(mouseX, mouseY, partialTicks);
    this.renderToolTips(mouseX, mouseY);
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  protected void renderToolTips(final int mouseX, final int mouseY) { }

  protected void renderFluidTankToolTip(final FluidTank tank, final int x, final int y) {
    this.renderTooltip(this.getFluidTankToolTip(tank), x, y);
  }

  public List<String> getFluidTankToolTip(final FluidTank tank) {
    final List<String> list = new ArrayList<>();

    if(tank.getFluid() != null) {
      list.add(tank.getFluid().getLocalizedName());
    } else {
      list.add(I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".fluid_empty"));
    }

    list.add(I18n.format(GradientBlocks.BRONZE_BOILER.getTranslationKey() + ".fluid_amount", tank.getFluidAmount(), tank.getCapacity()));

    return list;
  }


  public void drawSprite(final double xIn, final double yIn, final double width, final double height, final TextureAtlasSprite sprite, final int color, final double scaleIn) {
    GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
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

        buffer.pos(xS, yS, this.blitOffset).tex(uS, vS).color(r, g, b, a).endVertex();
        buffer.pos(xS, yE, this.blitOffset).tex(uS, vE).color(r, g, b, a).endVertex();
        buffer.pos(xE, yE, this.blitOffset).tex(uE, vE).color(r, g, b, a).endVertex();
        buffer.pos(xE, yS, this.blitOffset).tex(uE, vS).color(r, g, b, a).endVertex();
      }
    }

    tessellator.draw();
  }

  protected class ButtonItem extends Button {
    public final ItemStack item;

    public ButtonItem(final ItemStack item, final int x, final int y, final IPressable onPress) {
      super(x + GradientContainerScreen.this.guiLeft, y + GradientContainerScreen.this.guiTop, 20, 20, "", onPress);
      this.item = item;
    }

    @Override
    public void render(final int mouseX, final int mouseY, final float partialTicks) {
      super.render(mouseX, mouseY, partialTicks);

      if(this.visible) {
        GradientContainerScreen.this.itemRenderer.renderItemAndEffectIntoGUI(this.item, this.x + 2, this.y + 2);
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
      //TODO: fluids
/*
      if(this.tank.getFluid() == null || this.tank.getFluidAmount() == 0) {
        return;
      }

      final Fluid fluid = this.tank.getFluid().getFluid();
      final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(fluid.getStill(this.tank.getFluid()).toString());
      final double renderHeight = this.h * Util.limit(this.tank.getFluidAmount() / (double)this.tank.getCapacity(), 0.0d, 1.0d);
      final int color = fluid.getColor(this.tank.getFluid());

      Minecraft.getInstance().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      GradientGuiContainer.this.drawSprite(this.x, (this.y + this.h) - renderHeight, this.w, renderHeight, sprite, color, 1.0d);
*/
    }

    public boolean isMouseOver(final int mouseX, final int mouseY) {
      final int mX = mouseX - GradientContainerScreen.this.guiLeft;
      final int mY = mouseY - GradientContainerScreen.this.guiTop;
      return mX >= this.x && mY >= this.y && mX < this.x + this.w && mY < this.y + this.h;
    }
  }
}
