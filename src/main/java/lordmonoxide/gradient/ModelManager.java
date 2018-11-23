package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.dryingrack.TileDryingRack;
import lordmonoxide.gradient.blocks.dryingrack.TileDryingRackRenderer;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePitRenderer;
import lordmonoxide.gradient.blocks.manualgrinder.TileManualGrinder;
import lordmonoxide.gradient.blocks.manualgrinder.TileManualGrinderRenderer;
import lordmonoxide.gradient.blocks.mixingbasin.TileMixingBasin;
import lordmonoxide.gradient.blocks.mixingbasin.TileMixingBasinRenderer;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
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
    GradientMetals.metals.stream()
      .map(GradientMetals.Metal::getFluid)
      .forEach(ModelManager::registerFluidModel);
  }

  private static void registerFluidModel(final Fluid fluid) {
    final Item item = Item.getItemFromBlock(fluid.getBlock());
    assert item != Items.AIR;

    ModelBakery.registerItemVariants(item);

    final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, "fluid"), fluid.getName());

    ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

    ModelLoader.setCustomStateMapper(fluid.getBlock(), new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return modelResourceLocation;
      }
    });
  }

  private static void registerBlockModels() {
    ClientRegistry.bindTileEntitySpecialRenderer(TileFirePit.class, new TileFirePitRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileManualGrinder.class, new TileManualGrinderRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileMixingBasin.class, new TileMixingBasinRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileDryingRack.class, new TileDryingRackRenderer());
  }

  private static void registerItemModels() {
    // Register items with custom model names first
    //registerItemModel(ModItems.SNOWBALL_LAUNCHER, "minecraft:fishing_rod");

    //registerVariantItemModels(ModItems.VARIANTS_ITEM, "variant", ItemVariants.EnumType.values());

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

    if(!item.getHasSubtypes()) {
      ModelBakery.registerItemVariants(item, fullModelLocation);
      ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> fullModelLocation));
      return;
    }

    final NonNullList<ItemStack> stacks = NonNullList.create();
    item.getSubItems(item.getCreativeTab(), stacks);

    stacks.forEach(stack -> ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, item.getTranslationKey(stack).substring(5)), "inventory")));
  }

  /**
   * A hackish adapter that allows lambdas to be used as {@link ItemMeshDefinition} implementations without breaking ForgeGradle's
   * reobfuscation and causing {@link AbstractMethodError}s.
   * <p>
   * Written by diesieben07 in this thread:
   * http://www.minecraftforge.net/forum/index.php/topic,34034.0.html
   *
   * @author diesieben07
   */
  @FunctionalInterface
  interface MeshDefinitionFix extends ItemMeshDefinition {
    ModelResourceLocation getLocation(final ItemStack stack);

    // Helper method to easily create lambda instances of this class
    static ItemMeshDefinition create(final MeshDefinitionFix lambda) {
      return lambda;
    }

    @Override
    default ModelResourceLocation getModelLocation(final ItemStack stack) {
      return this.getLocation(stack);
    }
  }

  @FunctionalInterface
  public interface CustomModel {
    void registerCustomModels();
  }
}
