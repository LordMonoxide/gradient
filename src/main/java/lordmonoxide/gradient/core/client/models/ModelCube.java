package lordmonoxide.gradient.core.client.models;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelCube extends ModelBase {
  private final ModelRenderer renderer = new ModelRenderer(this, 0, 0);

  public ModelCube() {
    this.renderer.setTextureSize(16, 16);
    this.renderer.addBox(-8.0f, -8.0f, -8.0f, 16, 16, 16);
    this.renderer.setRotationPoint(8.0f, 8.0f, 8.0f);
  }

  public void render() {
    this.renderer.render(0.0625f);
  }
}
