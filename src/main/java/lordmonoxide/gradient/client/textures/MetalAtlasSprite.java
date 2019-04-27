package lordmonoxide.gradient.client.textures;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class MetalAtlasSprite extends TextureAtlasSprite {
  private static final int COLOUR_DIFFUSE  = 0xFFFF0000;
  private static final int COLOUR_SPECULAR = 0xFF00FF00;
  private static final int COLOUR_SHADOW1  = 0xFFFFFF00;
  private static final int COLOUR_SHADOW2  = 0xFF0000FF;
  private static final int COLOUR_EDGE1    = 0xFFFF00FF;
  private static final int COLOUR_EDGE2    = 0xFF00FFFF;
  private static final int COLOUR_EDGE3    = 0xFF0B00B5;

  private static final int ALPHA_MASK = 0xFF000000;

  private final Metal metal;
  private final ImmutableList<ResourceLocation> dependencies;

  public MetalAtlasSprite(final ResourceLocation spriteName, final int width, final int height, final Metal metal, final ResourceLocation... source) {
    super(spriteName, width, height);
    this.metal = metal;
    this.dependencies = ImmutableList.copyOf(source);
  }

  @Override
  public boolean hasCustomLoader(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location) {
    return true;
  }

  @Override
  public Collection<ResourceLocation> getDependencies() {
    return this.dependencies;
  }

  @Override
  public boolean load(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location, @Nonnull final Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
    GradientMod.logger.info("LOADING METAL ATLAS SPRITE {}", location);

    final List<TextureAtlasSprite> sprites = this.dependencies.stream().map(textureGetter).collect(Collectors.toList());

    final NativeImage frame = new NativeImage(this.width, this.height, true);

    for(final TextureAtlasSprite sprite : sprites) {
      // Can't find base texture?
      if(sprite.getFrameCount() == 0) {
        throw new RuntimeException(new FileNotFoundException("Could not find base sprite " + sprite + " while generating sprite " + this));
      }

      final NativeImage source = sprite.frames[0];

      for(int x = 0; x < this.width; x++) {
        for(int y = 0; y < this.height; y++) {
          if((source.getPixelRGBA(x, y) & ALPHA_MASK) != 0) {
            frame.setPixelRGBA(x, y, source.getPixelRGBA(x, y));
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_DIFFUSE) {
            frame.setPixelRGBA(x, y, this.metal.colourDiffuse);
            continue;
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_SPECULAR) {
            frame.setPixelRGBA(x, y, this.metal.colourSpecular);
            continue;
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_SHADOW1) {
            frame.setPixelRGBA(x, y, this.metal.colourShadow1);
            continue;
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_SHADOW2) {
            frame.setPixelRGBA(x, y, this.metal.colourShadow2);
            continue;
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_EDGE1) {
            frame.setPixelRGBA(x, y, this.metal.colourEdge1);
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_EDGE2) {
            frame.setPixelRGBA(x, y, this.metal.colourEdge2);
          }

          if(frame.getPixelRGBA(x, y) == COLOUR_EDGE3) {
            frame.setPixelRGBA(x, y, this.metal.colourEdge3);
          }
        }
      }
    }

    this.clearFramesTextureData();
    this.frames = new NativeImage[] {frame};
    return false;
  }
}
