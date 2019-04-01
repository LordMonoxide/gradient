package lordmonoxide.gradient.dynamicores;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
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

    final ResourceLocation oreLoc = GradientMod.resource("blocks/ore");
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

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final ResourceLocation source) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + ".png")) == null) {
      map.setTextureEntry(new ClonedAtlasSprite(GradientMod.resource(name.getPath()), source));
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation name, final GradientMetals.Metal metal, final ResourceLocation... sprites) {
    if(getResource(GradientMod.resource("textures/" + name.getPath() + '.' + metal.name + ".png")) == null) {
      map.setTextureEntry(new DynamicAtlasSprite(GradientMod.resource(name.getPath() + '.' + metal.name), metal, sprites.length == 0 ? new ResourceLocation[] {name} : sprites));
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
