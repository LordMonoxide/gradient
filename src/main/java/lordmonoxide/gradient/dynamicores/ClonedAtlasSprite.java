package lordmonoxide.gradient.dynamicores;

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
import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class ClonedAtlasSprite extends TextureAtlasSprite {
  private final ImmutableList<ResourceLocation> dependencies;

  public ClonedAtlasSprite(final ResourceLocation spriteName, final ResourceLocation source) {
    super(spriteName.toString());
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

    this.width = sprite.getIconWidth();
    this.height = sprite.getIconHeight();
    final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
    pixels[0] = new int[this.width * this.height];

    final int[][] sourcePixels = sprite.getFrameTextureData(0);

    System.arraycopy(sourcePixels[0], 0, pixels[0], 0, this.width * this.height);

    this.clearFramesTextureData();
    this.framesTextureData.add(pixels);
    return false;
  }
}
