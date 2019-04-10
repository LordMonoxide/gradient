package lordmonoxide.gradient.client.textures;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.GradientMetals;
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
public class OldMetalAtlasSprite extends TextureAtlasSprite {
  private static final int COLOUR_DIFFUSE  = 0xFFFF0000;
  private static final int COLOUR_SPECULAR = 0xFF00FF00;
  private static final int COLOUR_SHADOW1  = 0xFFFFFF00;
  private static final int COLOUR_SHADOW2  = 0xFF0000FF;
  private static final int COLOUR_EDGE1    = 0xFFFF00FF;
  private static final int COLOUR_EDGE2    = 0xFF00FFFF;
  private static final int COLOUR_EDGE3    = 0xFF0B00B5;

  private static final int ALPHA_MASK = 0xFF000000;

  private final GradientMetals.Metal metal;
  private final ImmutableList<ResourceLocation> dependencies;

  public OldMetalAtlasSprite(final ResourceLocation spriteName, final GradientMetals.Metal metal, final ResourceLocation... source) {
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
        throw new RuntimeException(new FileNotFoundException("Could not find base sprite " + sprite + " while generating sprite " + this));
      }

      final int[][] sourcePixels = sprite.getFrameTextureData(0);

      for(int p = 0; p < this.width * this.height; p++) {
        if((sourcePixels[0][p] & ALPHA_MASK) != 0) {
          pixels[0][p] = sourcePixels[0][p];
        }

        if(pixels[0][p] == COLOUR_DIFFUSE) {
          pixels[0][p] = this.metal.colourDiffuse;
          continue;
        }

        if(pixels[0][p] == COLOUR_SPECULAR) {
          pixels[0][p] = this.metal.colourSpecular;
          continue;
        }

        if(pixels[0][p] == COLOUR_SHADOW1) {
          pixels[0][p] = this.metal.colourShadow1;
          continue;
        }

        if(pixels[0][p] == COLOUR_SHADOW2) {
          pixels[0][p] = this.metal.colourShadow2;
          continue;
        }

        if(pixels[0][p] == COLOUR_EDGE1) {
          pixels[0][p] = this.metal.colourEdge1;
        }

        if(pixels[0][p] == COLOUR_EDGE2) {
          pixels[0][p] = this.metal.colourEdge2;
        }

        if(pixels[0][p] == COLOUR_EDGE3) {
          pixels[0][p] = this.metal.colourEdge3;
        }
      }
    }

    this.clearFramesTextureData();
    this.framesTextureData.add(pixels);
    return false;
  }
}
