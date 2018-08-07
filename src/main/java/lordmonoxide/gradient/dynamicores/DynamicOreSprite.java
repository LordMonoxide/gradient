package lordmonoxide.gradient.dynamicores;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class DynamicOreSprite extends TextureAtlasSprite {
  private static final int COLOUR_DIFFUSE  = 0xFFFF0000;
  private static final int COLOUR_SPECULAR = 0xFF00FF00;
  private static final int COLOUR_SHADOW2  = 0xFF0000FF;
  private static final int COLOUR_EDGE1    = 0xFFFF00FF;

  private final ResourceLocation ore = GradientMod.resource("blocks/ore");
  private final ImmutableList<ResourceLocation> dependencies = ImmutableList.of(this.ore);
  private final GradientMetals.Metal metal;

  public DynamicOreSprite(final ResourceLocation spriteName, final GradientMetals.Metal metal) {
    super(spriteName.toString());
    this.metal = metal;
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
    final TextureAtlasSprite sprite = textureGetter.apply(this.ore);
    this.width = sprite.getIconWidth();
    this.height = sprite.getIconHeight();
    final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
    pixels[0] = new int[this.width * this.height];

    final int[][] oldPixels = sprite.getFrameTextureData(0);
    System.arraycopy(oldPixels[0], 0, pixels[0], 0, this.width * this.height);

    for(int p = 0; p < this.width * this.height; p++) {
      if(pixels[0][p] == COLOUR_DIFFUSE) {
        pixels[0][p] = this.metal.colourDiffuse;
        continue;
      }

      if(pixels[0][p] == COLOUR_SPECULAR) {
        pixels[0][p] = this.metal.colourSpecular;
        continue;
      }

      if(pixels[0][p] == COLOUR_SHADOW2) {
        pixels[0][p] = this.metal.colourShadow2;
        continue;
      }

      if(pixels[0][p] == COLOUR_EDGE1) {
        pixels[0][p] = this.metal.colourEdge1;
      }
    }

    this.clearFramesTextureData();
    this.framesTextureData.add(pixels);
    return false;
  }
}
