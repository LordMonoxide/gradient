package lordmonoxide.gradient.dynamicores;

import lordmonoxide.gradient.GradientMetals;
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

    final ResourceLocation oreLoc = GradientMod.resource("blocks/ore");
    final ResourceLocation ingotLoc = GradientMod.resource("items/cast_item.ingot");
    final ResourceLocation hammerLoc = GradientMod.resource("items/cast_item.hammer");
    final ResourceLocation mattockLoc = GradientMod.resource("items/cast_item.mattock");
    final ResourceLocation pickaxeLoc = GradientMod.resource("items/cast_item.pickaxe");
    final ResourceLocation swordLoc = GradientMod.resource("items/cast_item.sword");

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      registerDynamicTextures(map, oreLoc, metal);
      registerDynamicTextures(map, ingotLoc, metal);
      registerDynamicTextures(map, hammerLoc, metal);
      registerDynamicTextures(map, mattockLoc, metal);
      registerDynamicTextures(map, pickaxeLoc, metal);
      registerDynamicTextures(map, swordLoc, metal);
    }
  }

  private static void registerDynamicTextures(final TextureMap map, final ResourceLocation source, final GradientMetals.Metal metal) {
    if(getResource(GradientMod.resource("textures/" + source.getPath() + '.' + metal.name + ".png")) == null) {
      map.setTextureEntry(new DynamicAtlasSprite(GradientMod.resource(source.getPath() + '.' + metal.name), source, metal));
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
