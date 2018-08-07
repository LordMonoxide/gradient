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

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(getResource(GradientMod.resource("textures/blocks/ore_" + metal.name + ".png")) == null) {
        map.setTextureEntry(new DynamicOreSprite(GradientMod.resource("blocks/ore_" + metal.name), metal));
      }
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
