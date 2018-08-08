package lordmonoxide.gradient.dynamicores;

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
    registerItem(DynamicModelLoader::acceptIngots);
    registerItem(DynamicModelLoader::acceptHammers);
    registerItem(DynamicModelLoader::acceptMattocks);
    registerItem(DynamicModelLoader::acceptPickaxes);
    registerItem(DynamicModelLoader::acceptSwords);
  }

  private static void registerBlock(final Predicate<ResourceLocation> accepts) {
    ModelLoaderRegistry.registerLoader(new DynamicModel(accepts, DynamicModelLoader::blockTextures, new ResourceLocation("minecraft:block/cube_all")));
  }

  private static void registerItem(final Predicate<ResourceLocation> accepts) {
    ModelLoaderRegistry.registerLoader(new DynamicModel(accepts, DynamicModelLoader::itemTextures, new ResourceLocation("builtin/generated"), true));
  }

  private static boolean acceptOres(final ResourceLocation loc) {
    return loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().startsWith("ore.");
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

  private static ImmutableMap<String, String> blockTextures(final ResourceLocation loc) {
    return ImmutableMap.of("all", GradientMod.resource("blocks/" + loc.getPath()).toString());
  }

  private static ImmutableMap<String, String> itemTextures(final ResourceLocation loc) {
    return ImmutableMap.of("layer0", GradientMod.resource("items/" + loc.getPath()).toString());
  }
}
