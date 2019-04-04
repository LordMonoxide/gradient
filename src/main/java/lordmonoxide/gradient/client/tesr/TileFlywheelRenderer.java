package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.tileentities.TileFlywheel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileFlywheelRenderer extends TileEntityRenderer<TileFlywheel> {
  @Override
  public void render(final TileFlywheel te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translated(x, y, z);

    this.drawNameplate(te, "Energy " + te.getEnergy(), 0.0d, 0.0d, 0.0d, 100);

    GlStateManager.popMatrix();
  }
}
