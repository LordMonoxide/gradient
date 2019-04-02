package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GradientMod.MODID)
public final class DynamicModelLoader {
  private DynamicModelLoader() { }

  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    GradientMod.logger.info("Registering dynamic models");

    registerBlock(DynamicModelLoader::acceptOres);
    registerBlock(DynamicModelLoader::acceptPebbles, GradientMod.resource("block/pebble")).disableRetexture();
    registerItem(DynamicModelLoader::acceptIngots);
    registerItem(DynamicModelLoader::acceptHammers);
    registerItem(DynamicModelLoader::acceptMattocks);
    registerItem(DynamicModelLoader::acceptPickaxes);
    registerItem(DynamicModelLoader::acceptSwords);
    registerItem(DynamicModelLoader::acceptNuggets);
    registerItem(DynamicModelLoader::acceptDusts);
    registerItem(DynamicModelLoader::acceptCrushed);
    registerItem(DynamicModelLoader::acceptPurified);
    registerItem(DynamicModelLoader::acceptPlates);
    registerItem(DynamicModelLoader::acceptTools);
  }

  private static DynamicModel registerBlock(final Predicate<ResourceLocation> accepts, final ResourceLocation baseModel) {
    final DynamicModel model = new DynamicModel(accepts, DynamicModelLoader::blockTextures, baseModel);
    ModelLoaderRegistry.registerLoader(model);
    return model;
  }

  private static DynamicModel registerBlock(final Predicate<ResourceLocation> accepts) {
    return registerBlock(accepts, new ResourceLocation("minecraft:block/cube_all"));
  }

  private static DynamicModel registerItem(final Predicate<ResourceLocation> accepts) {
    final DynamicModel model = new DynamicModel(accepts, DynamicModelLoader::itemTextures, new ResourceLocation("builtin/generated"), true);
    ModelLoaderRegistry.registerLoader(model);
    return model;
  }

  private static boolean acceptOres(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("ore.");
  }

  private static boolean acceptPebbles(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("pebble.");
  }

  private static boolean acceptIngots(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("cast_item.ingot.");
  }

  private static boolean acceptHammers(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("cast_item.hammer.");
  }

  private static boolean acceptMattocks(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("cast_item.mattock.");
  }

  private static boolean acceptPickaxes(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("cast_item.pickaxe.");
  }

  private static boolean acceptSwords(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("cast_item.sword.");
  }

  private static boolean acceptNuggets(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("nugget.");
  }

  private static boolean acceptDusts(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("dust.");
  }

  private static boolean acceptCrushed(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("crushed.");
  }

  private static boolean acceptPurified(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("purified.");
  }

  private static boolean acceptPlates(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("plate.");
  }

  private static boolean acceptTools(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("tool.");
  }

  private static ImmutableMap<String, String> blockTextures(final ResourceLocation loc) {
    return ImmutableMap.of("all", GradientMod.resource("blocks/" + loc.getPath()).toString());
  }

  private static ImmutableMap<String, String> itemTextures(final ResourceLocation loc) {
    return ImmutableMap.of("layer0", GradientMod.resource("items/" + loc.getPath()).toString());
  }
}
