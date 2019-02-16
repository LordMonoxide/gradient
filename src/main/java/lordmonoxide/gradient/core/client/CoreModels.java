package lordmonoxide.gradient.core.client;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.client.tesr.TileOreRenderer;
import lordmonoxide.gradient.core.tileentities.TileOre;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class CoreModels {
  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    GradientCore.LOGGER.info("REGISTERING CORE MODELS");

    //TODO: this is the worst way to do this ever
    ClientRegistry.bindTileEntitySpecialRenderer(TileOre.class, new TileOreRenderer());

    //TODO: once ICustomModelLoaders are implemented, I'll know, because this will explode

    ModelLoaderRegistry.registerLoader(new ICustomModelLoader() {
      @Override
      public void onResourceManagerReload(final IResourceManager resourceManager) {
        //throw new RuntimeException("Reload not implemented");
      }

      @Override
      public boolean accepts(final ResourceLocation modelLocation) {
        throw new RuntimeException("accepts " + modelLocation);
        //return false;
      }

      @Override
      public IUnbakedModel loadModel(final ResourceLocation modelLocation) throws Exception {
        throw new RuntimeException("loadModel " + modelLocation);
        //return null;
      }
    });
  }
}
