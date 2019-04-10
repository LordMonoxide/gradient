package lordmonoxide.gradient;

import lordmonoxide.gradient.client.tesr.TileClayOvenRenderer;
import lordmonoxide.gradient.client.tesr.TileDryingRackRenderer;
import lordmonoxide.gradient.client.tesr.TileFirePitRenderer;
import lordmonoxide.gradient.client.tesr.TileFlywheelRenderer;
import lordmonoxide.gradient.client.tesr.TileManualGrinderRenderer;
import lordmonoxide.gradient.client.tesr.TileMixingBasinRenderer;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.tileentities.TileClayOven;
import lordmonoxide.gradient.tileentities.TileDryingRack;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.tileentities.TileFlywheel;
import lordmonoxide.gradient.tileentities.TileManualGrinder;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GradientMod.MODID)
public final class ModelManager {
  private ModelManager() { }

  private static final Set<Item> itemsRegistered = new HashSet<>();

  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    GradientMod.logger.info("Registering models");

    registerFluidModels();
    registerBlockModels();
    registerItemModels();

    itemsRegistered.clear();
  }

  private static void registerFluidModels() {
    Metals.all().stream()
      .map(Metal::getFluid)
      .forEach(ModelManager::registerFluidModel);
  }

  private static void registerFluidModel(final Fluid fluid) {
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
    // Register items with custom model names first
    //registerItemModel(ModItems.SNOWBALL_LAUNCHER, "minecraft:fishing_rod");

    // Then register items with default model names
    GradientItems.ITEMS.stream()
      .filter(item -> !itemsRegistered.contains(item))
      .forEach(ModelManager::registerItemModel);
  }

  private static void registerItemModel(final Item item) {
    registerItemModel(item, item.getRegistryName().toString());
  }

  private static void registerItemModel(final Item item, final String modelLocation) {
    final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
    registerItemModel(item, fullModelLocation);
  }

  private static void registerItemModel(final Item item, final ModelResourceLocation fullModelLocation) {
    itemsRegistered.add(item);

    if(item instanceof CustomModel) {
      ((CustomModel)item).registerCustomModels();
      return;
    }

    if(item.getHasSubtypes()) {
      ModelBakery.registerItemVariants(item, fullModelLocation);
      ModelLoader.setCustomMeshDefinition(item, stack -> fullModelLocation);
      return;
    }

    ModelLoader.setCustomModelResourceLocation(item, 0, fullModelLocation);
  }

  private static <T extends Comparable<T>> void registerVariantItemModels(final Item item, final String variantName, final IProperty<T> variants) {
    int i = 0;

    for(final T variant : variants.getAllowedValues()) {
      registerItemModelForMeta(item, i++, variantName + '=' + variants.getName(variant));
    }
  }

  private static void registerItemModelForMeta(final Item item, final int metadata, final String variant) {
    registerItemModelForMeta(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
  }

  private static void registerItemModelForMeta(final Item item, final int metadata, final ModelResourceLocation modelResourceLocation) {
    itemsRegistered.add(item);
    ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
  }

  @FunctionalInterface
  public interface CustomModel {
    void registerCustomModels();
  }
}
