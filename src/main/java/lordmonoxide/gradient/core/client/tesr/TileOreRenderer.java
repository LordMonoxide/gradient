package lordmonoxide.gradient.core.client.tesr;

import lordmonoxide.gradient.core.client.models.ModelCube;
import lordmonoxide.gradient.core.tileentities.TileOre;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileOreRenderer extends TileEntityRenderer<TileOre> {
  private final ModelCube model = new ModelCube();

  @Override
  public void render(final TileOre ore, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    this.setLightmapDisabled(true);

    if(ore.getOreTexture() == null) {
      return;
    }

    this.bindTexture(ore.getOreTexture());

    GlStateManager.pushMatrix();
    GlStateManager.translated(x, y, z);

    this.model.render();

    GlStateManager.popMatrix();
  }
}
