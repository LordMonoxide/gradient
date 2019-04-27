package lordmonoxide.gradient.client.textures;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class BlendedMetalAtlasSprite extends TextureAtlasSprite {
  private static final int ALPHA_MASK = 0xFF000000;
  private static final int COLOUR_MASK = 0xFFFFFF;
  private static final float COLOUR_DIVISOR = 0xFFFFFF;

  private final Metal metal;
  private final Function<Float, Float> mix;
  private final ImmutableList<ResourceLocation> dependencies;

  public BlendedMetalAtlasSprite(final ResourceLocation spriteName, final int width, final int height, final Metal metal, final Function<Float, Float> mix, final ResourceLocation source) {
    super(spriteName, width, height);
    this.metal = metal;
    this.mix = mix;
    this.dependencies = ImmutableList.of(source);
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
    final ResourceLocation source = this.dependencies.get(0);
    final TextureAtlasSprite sprite = textureGetter.apply(source);

    this.clearFramesTextureData();

    try {
      // This is a hack to force it to load the animation data
      this.loadSpriteFrames(DynamicTextureLoader.getResource(new ResourceLocation(source.getNamespace(), "textures/" + source.getPath() + ".png")), Minecraft.getInstance().gameSettings.mipmapLevels + 1);
    } catch(final Exception e) {
      GradientMod.logger.warn(e);
    }

    this.frames = new NativeImage[sprite.getFrameCount()];

    for(int frameIndex = 0; frameIndex < sprite.getFrameCount(); frameIndex++) {
      final NativeImage newFrame = new NativeImage(this.width, this.height, true);
      final NativeImage oldFrame = sprite.frames[frameIndex];

      for(int x = 0; x < this.width; x++) {
        for(int y = 0; y < this.height; y++) {
          newFrame.setPixelRGBA(x, y, this.mix(this.metal.colourDiffuse, oldFrame.getPixelRGBA(x, y)));
        }
      }

      this.frames[frameIndex] = newFrame;
    }

    return false;
  }

  private int mix(final int source, final int mix) {
    final int sourceAlpha = source & ALPHA_MASK;
    final int sourceColour = source & COLOUR_MASK;
    final float ratio = (mix & COLOUR_MASK) / COLOUR_DIVISOR;

    return sourceAlpha | this.blend(0, sourceColour, this.mix.apply(ratio));
  }

  private int blend(final int c1, final int c2, final float ratio) {
    final float iRatio = 1.0f - ratio;

    final int r1 = (c1 & 0xff0000) >> 16;
    final int g1 = (c1 & 0xff00) >> 8;
    final int b1 = (c1 & 0xff);

    final int r2 = (c2 & 0xff0000) >> 16;
    final int g2 = (c2 & 0xff00) >> 8;
    final int b2 = (c2 & 0xff);

    final int r = (int)(r1 * iRatio + r2 * ratio);
    final int g = (int)(g1 * iRatio + g2 * ratio);
    final int b = (int)(b1 * iRatio + b2 * ratio);

    return r << 16 | g << 8 | b;
  }

  public static float identity(final float ratio) {
    return ratio;
  }

  public static float enhance(final float ratio) {
    return Math.max(0.0f, Math.min(1.0f, (ratio - 0.7f) * 2.0f + 0.5f));
  }
}
