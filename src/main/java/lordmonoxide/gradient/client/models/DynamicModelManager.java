package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GradientMod.MOD_ID)
public final class DynamicModelManager {
  private DynamicModelManager() { }

  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    GradientMod.logger.info("Registering dynamic models");

    registerBlock(DynamicModelManager::acceptOres);
    registerBlock(DynamicModelManager::acceptCastBlocks);
    registerBlock(DynamicModelManager::acceptPebbles, GradientMod.resource("block/pebble")).disableRetexture();
    registerItem(DynamicModelManager::acceptIngots);
    registerItem(DynamicModelManager::acceptHammers);
    registerItem(DynamicModelManager::acceptMattocks);
    registerItem(DynamicModelManager::acceptPickaxes);
    registerItem(DynamicModelManager::acceptSwords);
    registerItem(DynamicModelManager::acceptNuggets);
    registerItem(DynamicModelManager::acceptAlloyNuggets);
    registerItem(DynamicModelManager::acceptDusts);
    registerItem(DynamicModelManager::acceptCrushed);
    registerItem(DynamicModelManager::acceptPurified);
    registerItem(DynamicModelManager::acceptPlates);
    registerItem(DynamicModelManager::acceptTools);
    registerItem(DynamicModelManager::acceptBark);
  }

  private static DynamicModelLoader registerBlock(final Predicate<ResourceLocation> accepts, final ResourceLocation baseModel) {
    final DynamicModelLoader model = new DynamicModelLoader(accepts, DynamicModelManager::blockTextures, baseModel);
    ModelLoaderRegistry.registerLoader(model);
    return model;
  }

  private static DynamicModelLoader registerBlock(final Predicate<ResourceLocation> accepts) {
    return registerBlock(accepts, new ResourceLocation("minecraft:block/cube_all"));
  }

  private static DynamicModelLoader registerItem(final Predicate<ResourceLocation> accepts) {
    final DynamicModelLoader model = new DynamicModelLoader(accepts, DynamicModelManager::itemTextures, new ResourceLocation("builtin/generated"), true);
    ModelLoaderRegistry.registerLoader(model);
    return model;
  }

  private static boolean acceptOres(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("ore.");
  }

  private static boolean acceptCastBlocks(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_block.");
  }

  private static boolean acceptPebbles(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("pebble.");
  }

  private static boolean acceptIngots(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_item.ingot.");
  }

  private static boolean acceptHammers(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_item.hammer.");
  }

  private static boolean acceptMattocks(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_item.mattock.");
  }

  private static boolean acceptPickaxes(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_item.pickaxe.");
  }

  private static boolean acceptSwords(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("cast_item.sword.");
  }

  private static boolean acceptNuggets(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("nugget.");
  }

  private static boolean acceptAlloyNuggets(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("alloy_nugget.");
  }

  private static boolean acceptDusts(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("dust.");
  }

  private static boolean acceptCrushed(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("crushed.");
  }

  private static boolean acceptPurified(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("purified.");
  }

  private static boolean acceptPlates(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("plate.");
  }

  private static boolean acceptTools(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("tool.");
  }

  private static boolean acceptBark(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MOD_ID) && loc.getPath().startsWith("bark_");
  }

  private static ImmutableMap<String, String> blockTextures(final ResourceLocation loc) {
    return ImmutableMap.of("all", GradientMod.resource("block/" + loc.getPath()).toString());
  }

  private static ImmutableMap<String, String> itemTextures(final ResourceLocation loc) {
    return ImmutableMap.of("layer0", GradientMod.resource("item/" + loc.getPath()).toString());
  }
}
