package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.tileentities.TileFlywheel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileFlywheelRenderer extends TileEntitySpecialRenderer<TileFlywheel> {
  @Override
  public void render(final TileFlywheel te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(x, y, z);

    this.drawNameplate(te, "Energy " + te.getEnergy(), 0.0d, 0.0d, 0.0d, 100);

    GlStateManager.popMatrix();
  }
}
