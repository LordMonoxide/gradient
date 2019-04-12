package lordmonoxide.gradient.client.textures;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class BlendedMetalAtlasSprite extends TextureAtlasSprite {
  private static final int ALPHA_MASK = 0xFF000000;
  private static final int COLOUR_MASK = 0xFFFFFF;
  private static final float COLOUR_DIVISOR = 0xFFFFFF;

  private final Metal metal;
  private final ImmutableList<ResourceLocation> dependencies;

  public BlendedMetalAtlasSprite(final ResourceLocation spriteName, final Metal metal, final ResourceLocation... source) {
    super(spriteName.toString());
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
    final List<TextureAtlasSprite> sprites = this.dependencies.stream().map(textureGetter).collect(Collectors.toList());

    this.width = sprites.get(0).getIconWidth();
    this.height = sprites.get(0).getIconHeight();
    final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
    pixels[0] = new int[this.width * this.height];

    for(final TextureAtlasSprite sprite : sprites) {
      // Can't find base texture?
      if(sprite.getFrameCount() == 0) {
        throw new RuntimeException(new FileNotFoundException("Could not find blend sprite " + sprite + " while generating sprite " + this));
      }

      final int[][] sourcePixels = sprite.getFrameTextureData(0);

      for(int p = 0; p < this.width * this.height; p++) {
        pixels[0][p] = this.mix(this.metal.colourDiffuse, sourcePixels[0][p]);
      }
    }

    this.clearFramesTextureData();
    this.framesTextureData.add(pixels);
    return false;
  }

  private int mix(final int source, final int mix) {
    final int sourceAlpha = source & ALPHA_MASK;
    final int sourceColour = source & COLOUR_MASK;
    final float mixPercent = (mix & COLOUR_MASK) / COLOUR_DIVISOR;

    return sourceAlpha | this.blend(0, sourceColour, Math.min(1.0f, (mixPercent - 0.7f) * 2.0f + 0.5f));
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
}
