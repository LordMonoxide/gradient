package lordmonoxide.gradient.client.textures;

import com.google.common.collect.ImmutableList;
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
public class DynamicAtlasSprite extends TextureAtlasSprite {
  private final ImmutableList<ResourceLocation> dependencies;
  private final TextureGenerator generator;

  public DynamicAtlasSprite(final ResourceLocation spriteName, final TextureGenerator generator, final ResourceLocation... source) {
    super(spriteName.toString());
    this.generator = generator;
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
    }

    this.generator.generate(sprites, pixels);
    this.clearFramesTextureData();
    this.framesTextureData.add(pixels);
    return false;
  }

  @FunctionalInterface
  public interface TextureGenerator {
    void generate(final List<TextureAtlasSprite> sprite, final int[][] out);
  }
}
