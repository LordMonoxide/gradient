package lordmonoxide.gradient.core.client.tesr;

import lordmonoxide.gradient.core.tileentities.TileOre;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileOreRenderer extends TileEntityRenderer<TileOre> {
  @Override
  public void render(final TileOre ore, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    if(ore.getOre() != null) {
      this.drawNameplate(ore, ore.getOre().name.toString(), x, y, z, 1000);
    }
  }
}
