package lordmonoxide.gradient.core.client.textures;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.geology.elements.Metal;
import lordmonoxide.gradient.core.geology.ores.Ore;
import lordmonoxide.gradient.core.geology.ores.Ores;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DynamicTextureLoader {
  private DynamicTextureLoader() { }

  private static final int COLOUR_DIFFUSE  = 0xFFFF0000;
  private static final int COLOUR_SPECULAR = 0xFF00FF00;
  private static final int COLOUR_SHADOW1  = 0xFFFFFF00;
  private static final int COLOUR_SHADOW2  = 0xFF0000FF;
  private static final int COLOUR_EDGE1    = 0xFFFF00FF;
  private static final int COLOUR_EDGE2    = 0xFF00FFFF;
  private static final int COLOUR_EDGE3    = 0xFF0B00B5;

  @SubscribeEvent
  public static void onTextureStitch(final TextureStitchEvent.Pre event) {
    final ResourceLocation oreLoc = GradientCore.resource("textures/blocks/ore.png");

    for(final Ore ore : Ores.all()) {
      final ResourceLocation loc = new ResourceLocation(ore.name.getNamespace(), "textures/blocks/ore." + ore.name.getPath() + ".png");

      Minecraft.getInstance().getTextureManager().loadTexture(loc, new AbstractTexture() {
        @Override
        public void loadTexture(final IResourceManager manager) throws IOException {
          GradientCore.LOGGER.info("GENERATING TEX {}", loc);

          try(
            final IResource resource = manager.getResource(oreLoc);
            final NativeImage image = NativeImage.read(resource.getInputStream())
          ) {
            boolean blur = false;
            boolean clamp = false;

            if(resource.hasMetadata()) {
              final TextureMetadataSection meta = resource.getMetadata(TextureMetadataSection.SERIALIZER);

              if(meta != null) {
                blur = meta.getTextureBlur();
                clamp = meta.getTextureClamp();
              }
            }

            final Metal metal = (Metal)ore.rawElement;

            for(int x = 0; x < image.getWidth(); x++) {
              for(int y = 0; y < image.getHeight(); y++) {
                if(image.getPixelRGBA(x, y) == COLOUR_DIFFUSE) {
                  image.setPixelRGBA(x, y, metal.colourDiffuse);
                  continue;
                }

                if(image.getPixelRGBA(x, y) == COLOUR_SPECULAR) {
                  image.setPixelRGBA(x, y, metal.colourSpecular);
                  continue;
                }

                if(image.getPixelRGBA(x, y) == COLOUR_SHADOW1) {
                  image.setPixelRGBA(x, y, metal.colourShadow1);
                  continue;
                }

                if(image.getPixelRGBA(x, y) == COLOUR_SHADOW2) {
                  image.setPixelRGBA(x, y, metal.colourShadow2);
                  continue;
                }

                if(image.getPixelRGBA(x, y) == COLOUR_EDGE1) {
                  image.setPixelRGBA(x, y, metal.colourEdge1);
                }

                if(image.getPixelRGBA(x, y) == COLOUR_EDGE2) {
                  image.setPixelRGBA(x, y, metal.colourEdge2);
                }

                if(image.getPixelRGBA(x, y) == COLOUR_EDGE3) {
                  image.setPixelRGBA(x, y, metal.colourEdge3);
                }
              }
            }

            this.bindTexture();
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), 0, image.getWidth(), image.getHeight());
            image.uploadTextureSub(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), blur, clamp, false);
          }
        }
      });
    }
  }
}
