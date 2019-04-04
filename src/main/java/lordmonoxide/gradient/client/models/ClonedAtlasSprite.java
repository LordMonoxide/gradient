package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class ClonedAtlasSprite extends TextureAtlasSprite {
  private final ImmutableList<ResourceLocation> dependencies;

  public ClonedAtlasSprite(final ResourceLocation spriteName, final ResourceLocation source, final int width, final int height) {
    super(spriteName, width, height);
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
    final TextureAtlasSprite sprite = textureGetter.apply(this.dependencies.get(0));

    // Can't find base texture?
    if(sprite.getFrameCount() == 0) {
      throw new RuntimeException(new FileNotFoundException("Could not find base sprite " + sprite + " while generating sprite " + this));
    }

    final int[][] pixels = new int[Minecraft.getInstance().gameSettings.mipmapLevels + 1][];
    pixels[0] = new int[this.width * this.height];

    //TODO final int[][] sourcePixels = sprite.getFrameTextureData(0);

    //TODO System.arraycopy(sourcePixels[0], 0, pixels[0], 0, this.width * this.height);

    this.clearFramesTextureData();
    //TODO this.framesTextureData.add(pixels);
    return false;
  }
}
