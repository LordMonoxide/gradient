package lordmonoxide.gradient.blocks.kinetic.flywheel;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileFlywheelRenderer extends TileEntitySpecialRenderer<TileFlywheel> {
  @Override
  public void render(final TileFlywheel te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    this.drawNameplate(te, "Energy " + te.getEnergy(), 0.0d, 0.0d, 0.0d, 100);

    GlStateManager.popMatrix();
  }
}
