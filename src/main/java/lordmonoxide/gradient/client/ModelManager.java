package lordmonoxide.gradient.client;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.client.models.ModelClayBucket;
import lordmonoxide.gradient.client.tesr.TileClayOvenRenderer;
import lordmonoxide.gradient.client.tesr.TileDryingRackRenderer;
import lordmonoxide.gradient.client.tesr.TileFirePitRenderer;
import lordmonoxide.gradient.client.tesr.TileFlywheelRenderer;
import lordmonoxide.gradient.client.tesr.TileManualGrinderRenderer;
import lordmonoxide.gradient.client.tesr.TileMixingBasinRenderer;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.tileentities.TileClayOven;
import lordmonoxide.gradient.tileentities.TileDryingRack;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.tileentities.TileFlywheel;
import lordmonoxide.gradient.tileentities.TileManualGrinder;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModelManager {
  private ModelManager() { }

  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    GradientMod.logger.info("Registering models");

    registerFluidModels();
    registerBlockModels();
    registerItemModels();
  }

  private static void registerFluidModels() {
    GradientMetals.metals.stream()
      .map(GradientMetals.Metal::getFluid)
      .forEach(ModelManager::registerFluidModel);
  }

  private static void registerFluidModel(final Fluid fluid) {
    //TODO
/*
    final Item item = Item.getItemFromBlock(fluid.getBlock());
    assert item != Items.AIR;

    ModelBakery.registerItemVariants(item);

    final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, "fluid"), fluid.getName());

    registerItemModel(item, modelResourceLocation);

    ModelLoader.setCustomStateMapper(fluid.getBlock(), new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return modelResourceLocation;
      }
    });
*/
  }

  private static void registerBlockModels() {
    ClientRegistry.bindTileEntitySpecialRenderer(TileFirePit.class, new TileFirePitRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileClayOven.class, new TileClayOvenRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileManualGrinder.class, new TileManualGrinderRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMixingBasin.class, new TileMixingBasinRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TileDryingRackRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileFlywheel.class, new TileFlywheelRenderer());
  }

  private static void registerItemModels() {
    ModelClayBucket.setBucketModelDefinition(GradientItems.CLAY_BUCKET);
  }
}
