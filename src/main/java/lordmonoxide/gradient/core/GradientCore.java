package lordmonoxide.gradient.core;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.core.blocks.CoreBlocks;
import lordmonoxide.gradient.core.geology.elements.Elements;
import lordmonoxide.gradient.core.geology.ores.Ores;
import net.minecraft.init.Blocks;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraft.world.gen.feature.TallGrassConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GradientCore.MODID)
public class GradientCore {
  public static final String MODID = "gradient-core";

  public static final Logger LOGGER = LogManager.getLogger();

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }

  public GradientCore() {
    FMLModLoadingContext.get().getModEventBus().addListener(this::worldGen);
    FMLModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    FMLModLoadingContext.get().getModEventBus().addListener(this::setupServer);
    FMLModLoadingContext.get().getModEventBus().register(this);
  }

  private void worldGen(final FMLCommonSetupEvent event) {
    for(final Biome biome : Biome.BIOMES) {
      biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createCompositeFeature(Feature.TALL_GRASS, new TallGrassConfig(CoreBlocks.PEBBLE.getDefaultState()), Biome.TWICE_SURFACE_WITH_CHANCE, new ChanceConfig(12)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(Feature.SPHERE_REPLACE, new SphereReplaceConfig(CoreBlocks.SALT, 4, 1, Lists.newArrayList(Blocks.DIRT, CoreBlocks.SALT)), Biome.TOP_SOLID, new FrequencyConfig(1)));
    }
  }

  private void setupClient(final FMLClientSetupEvent event) {
    registerResourceLoader((IReloadableResourceManager)event.getMinecraftSupplier().get().getResourceManager());
  }

  private void setupServer(final FMLDedicatedServerSetupEvent event) {
    registerResourceLoader(event.getServerSupplier().get().getResourceManager());
  }

  private static void registerResourceLoader(final IReloadableResourceManager resourceManager) {
    resourceManager.addReloadListener(Elements::reload);
    resourceManager.addReloadListener(Ores::reload);
  }
}
