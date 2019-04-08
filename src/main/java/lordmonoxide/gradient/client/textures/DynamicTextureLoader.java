package lordmonoxide.gradient.client.textures;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GradientMod.MODID)
public final class DynamicTextureLoader {
  private DynamicTextureLoader() { }

  @SubscribeEvent
  public static void onTextureStitch(final TextureStitchEvent.Pre event) {
    final TextureMap map = event.getMap();

    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_oak"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_oak"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_spruce"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_spruce"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_birch"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_birch"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_jungle"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_jungle"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_acacia"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_acacia"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("item/bark_dark_oak"), GradientMod.resource("item/bark_cutout"), new ResourceLocation("minecraft", "block/log_big_oak"));

    final ResourceLocation oreLoc = GradientMod.resource("block/ore");
    final ResourceLocation ingotLoc = GradientMod.resource("item/cast_item.ingot");
    final ResourceLocation hammerLoc = GradientMod.resource("item/cast_item.hammer");
    final ResourceLocation mattockLoc = GradientMod.resource("item/cast_item.mattock");
    final ResourceLocation pickaxeLoc = GradientMod.resource("item/cast_item.pickaxe");
    final ResourceLocation swordLoc = GradientMod.resource("item/cast_item.sword");
    final ResourceLocation nuggetLoc = GradientMod.resource("item/nugget");
    final ResourceLocation dustLoc = GradientMod.resource("item/dust");
    final ResourceLocation crushedLoc = GradientMod.resource("item/crushed");
    final ResourceLocation purifiedLoc = GradientMod.resource("item/purified");
    final ResourceLocation plateLoc = GradientMod.resource("item/plate");
    final ResourceLocation toolHandleLoc = GradientMod.resource("item/tool_handle");

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      registerDynamicTextures(map, oreLoc, metal);
      registerDynamicTextures(map, ingotLoc, metal);
      registerDynamicTextures(map, hammerLoc, metal);
      registerDynamicTextures(map, mattockLoc, metal);
      registerDynamicTextures(map, pickaxeLoc, metal);
      registerDynamicTextures(map, swordLoc, metal);
      registerDynamicTextures(map, nuggetLoc, metal);
      registerDynamicTextures(map, dustLoc, metal);
      registerDynamicTextures(map, crushedLoc, metal);
      registerDynamicTextures(map, purifiedLoc, metal);
      registerDynamicTextures(map, plateLoc, metal);

      for(final GradientTools.Type toolType : GradientTools.types()) {
        final ResourceLocation handleLoc = GradientMod.resource("item/tool_handle." + toolType.cast.name);
        final ResourceLocation headLoc = GradientMod.resource("item/cast_item." + toolType.cast.name);
        final ResourceLocation toolLoc = GradientMod.resource("item/tool." + toolType.cast.name);

        registerDynamicTextures(map, handleLoc, toolHandleLoc);

        if(toolType == GradientTools.SWORD) {
          registerDynamicTextures(map, toolLoc, metal, headLoc, handleLoc);
        } else {
          registerDynamicTextures(map, toolLoc, metal, handleLoc, headLoc);
        }
      }
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final DynamicAtlasSprite.TextureGenerator generator, final ResourceLocation name, final ResourceLocation... sprites) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + ".png")) == null) {
      final ResourceLocation loc = GradientMod.resource(name.getPath());
      GradientMod.logger.info("Registering dynamic texture {} ", loc);
      final Map<ResourceLocation, TextureAtlasSprite> textures = ObfuscationReflectionHelper.getPrivateValue(TextureMap.class, map, "mapUploadedSprites");
      textures.put(loc, new DynamicAtlasSprite(loc, 16, 16, generator, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final ResourceLocation source) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + ".png")) == null) {
      //TODO map.setTextureEntry(new ClonedAtlasSprite(GradientMod.resource(name.getPath()), source));
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final GradientMetals.Metal metal, final ResourceLocation... sprites) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + '.' + metal.name + ".png")) == null) {
      final ResourceLocation loc = GradientMod.resource(name.getPath() + '.' + metal.name);
      final Map<ResourceLocation, TextureAtlasSprite> textures = ObfuscationReflectionHelper.getPrivateValue(TextureMap.class, map, "mapUploadedSprites");
      textures.put(loc, new MetalAtlasSprite(loc, 16, 16, metal, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
    }
  }

  @Nullable
  private static IResource getResource(final ResourceLocation resourceLocation) {
    try {
      return Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
    } catch(final IOException ignored) {
      return null;
    }
  }

  private static void barkGenerator(final List<TextureAtlasSprite> sprites, final int[][] out) {
    //TODO
/*
    final int[][] cutout = sprites.get(0).getFrameTextureData(0);
    final int[][] bark = sprites.get(1).getFrameTextureData(0);

    for(int p = 0; p < bark[0].length; p++) {
      if(cutout[0][p] == -1) {
        out[0][p] = bark[0][p];
      }
    }
*/
  }
}
