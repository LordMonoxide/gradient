package lordmonoxide.gradient.client.textures;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.client.models.ClonedAtlasSprite;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import java.util.List;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GradientMod.MODID)
public final class DynamicTextureLoader {
  private DynamicTextureLoader() { }

  @SubscribeEvent
  public static void onTextureStitch(final TextureStitchEvent.Pre event) {
    final TextureMap map = event.getMap();

    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_oak"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_oak"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_spruce"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_spruce"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_birch"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_birch"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_jungle"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_jungle"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_acacia"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_acacia"));
    registerDynamicTextures(map, DynamicTextureLoader::barkGenerator, GradientMod.resource("items/bark_dark_oak"), GradientMod.resource("items/bark_cutout"), new ResourceLocation("minecraft", "blocks/log_big_oak"));

    final ResourceLocation oreLoc = GradientMod.resource("blocks/ore");
    final ResourceLocation castBlockLoc = GradientMod.resource("blocks/cast_block");
    final ResourceLocation fluidLoc = GradientMod.resource("blocks/fluid");
    final ResourceLocation ingotLoc = GradientMod.resource("items/cast_item.ingot");
    final ResourceLocation hammerLoc = GradientMod.resource("items/cast_item.hammer");
    final ResourceLocation mattockLoc = GradientMod.resource("items/cast_item.mattock");
    final ResourceLocation pickaxeLoc = GradientMod.resource("items/cast_item.pickaxe");
    final ResourceLocation swordLoc = GradientMod.resource("items/cast_item.sword");
    final ResourceLocation nuggetLoc = GradientMod.resource("items/nugget");
    final ResourceLocation dustLoc = GradientMod.resource("items/dust");
    final ResourceLocation crushedLoc = GradientMod.resource("items/crushed");
    final ResourceLocation purifiedLoc = GradientMod.resource("items/purified");
    final ResourceLocation plateLoc = GradientMod.resource("items/plate");
    final ResourceLocation toolHandleLoc = GradientMod.resource("items/tool_handle");

    for(final Ore.Metal ore : Ores.metals()) {
      registerDynamicTextures(map, oreLoc, ore.metal);
      registerDynamicTextures(map, crushedLoc, ore.metal);
      registerDynamicTextures(map, purifiedLoc, ore.metal);
    }

    for(final Metal metal : Metals.all()) {
      registerDynamicTextures(map, ingotLoc, metal);
      registerDynamicTextures(map, hammerLoc, metal);
      registerDynamicTextures(map, mattockLoc, metal);
      registerDynamicTextures(map, pickaxeLoc, metal);
      registerDynamicTextures(map, swordLoc, metal);
      registerDynamicTextures(map, nuggetLoc, metal);
      registerDynamicTextures(map, dustLoc, metal);
      registerDynamicTextures(map, plateLoc, metal);
      registerBlendedMetalTextures(map, castBlockLoc, metal);
      registerBlendedMetalFluidTextures(map, fluidLoc, metal);

      for(final GradientTools.Type toolType : GradientTools.types()) {
        final ResourceLocation handleLoc = GradientMod.resource("items/tool_handle." + toolType.cast.name);
        final ResourceLocation headLoc = GradientMod.resource("items/cast_item." + toolType.cast.name);
        final ResourceLocation toolLoc = GradientMod.resource("items/tool." + toolType.cast.name);

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
      map.setTextureEntry(new DynamicAtlasSprite(GradientMod.resource(name.getPath()), generator, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final ResourceLocation source) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + ".png")) == null) {
      map.setTextureEntry(new ClonedAtlasSprite(GradientMod.resource(name.getPath()), source));
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final Metal metal, final ResourceLocation... sprites) {
    if(getResource(new ResourceLocation(name.getNamespace(), "textures/" + name.getPath() + '.' + metal.name + ".png")) == null) {
      map.setTextureEntry(new MetalAtlasSprite(new ResourceLocation(name.getNamespace(), name.getPath() + '.' + metal.name), metal, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
    }
  }

  private static void registerBlendedMetalTextures(final TextureMap map, final ResourceLocation name, final Metal metal) {
    if(getResource(new ResourceLocation(name.getNamespace(), "textures/" + name.getPath() + '.' + metal.name + ".png")) == null) {
      map.setTextureEntry(new BlendedMetalAtlasSprite(new ResourceLocation(name.getNamespace(), name.getPath() + '.' + metal.name), metal, BlendedMetalAtlasSprite::enhance, name));
    }
  }

  private static void registerBlendedMetalFluidTextures(final TextureMap map, final ResourceLocation name, final Metal metal) {
    if(getResource(new ResourceLocation(name.getNamespace(), "textures/" + name.getPath() + '_' + metal.name + ".png")) == null) {
      map.setTextureEntry(new BlendedMetalAtlasSprite(new ResourceLocation(name.getNamespace(), name.getPath() + '_' + metal.name), metal, BlendedMetalAtlasSprite::identity, name));
    }

    if(getResource(new ResourceLocation(name.getNamespace(), "textures/" + name.getPath() + '_' + metal.name + "_flowing.png")) == null) {
      map.setTextureEntry(new BlendedMetalAtlasSprite(new ResourceLocation(name.getNamespace(), name.getPath() + '_' + metal.name + "_flowing"), metal, BlendedMetalAtlasSprite::identity, new ResourceLocation(name.getNamespace(), name.getPath() + "_flowing")));
    }
  }

  @Nullable
  public static IResource getResource(final ResourceLocation resourceLocation) {
    try {
      return Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
    } catch(final IOException ignored) {
      return null;
    }
  }

  private static void barkGenerator(final List<TextureAtlasSprite> sprites, final int[][] out) {
    final int[][] cutout = sprites.get(0).getFrameTextureData(0);
    final int[][] bark = sprites.get(1).getFrameTextureData(0);

    for(int p = 0; p < bark[0].length; p++) {
      if(cutout[0][p] == -1) {
        out[0][p] = bark[0][p];
      }
    }
  }
}
