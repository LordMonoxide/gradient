package lordmonoxide.gradient.client.rendering;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GradientMod.MODID)
public final class DynamicTextureLoader {
  private DynamicTextureLoader() { }

  @SubscribeEvent
  public static void onTextureStitch(final TextureStitchEvent.Pre event) {
    final TextureMap map = event.getMap();

    final DynamicAtlasSprite.TextureGenerator barkGenerator = (sprites, out) -> {
      final int[][] cutout = sprites.get(0).getFrameTextureData(0);
      final int[][] bark = sprites.get(1).getFrameTextureData(0);

      for(int p = 0; p < bark[0].length; p++) {
        if(cutout[0][p] == -1) {
          out[0][p] = bark[0][p];
        }
      }
    };

    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_oak"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_oak"));
    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_spruce"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_spruce"));
    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_birch"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_birch"));
    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_jungle"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_jungle"));
    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_acacia"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_acacia"));
    registerDynamicTextures(map, barkGenerator, GradientMod.resource("items/bark_dark_oak"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_big_oak"));
  }

  private static void registerDynamicTextures(final TextureMap map, final DynamicAtlasSprite.TextureGenerator generator, final ResourceLocation name, final ResourceLocation... sprites) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + ".png")) == null) {
      map.setTextureEntry(new DynamicAtlasSprite(GradientMod.resource(name.getPath()), generator, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
    }
  }

  @Nullable
  private static IResource getResource(final ResourceLocation resourceLocation) {
    try {
      return Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
    } catch(final IOException ignored) {
      return null;
    }
  }
}
