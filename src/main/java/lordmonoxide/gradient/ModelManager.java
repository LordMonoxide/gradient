package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.state.IBlockState;
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

    ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);

    ModelLoader.setCustomStateMapper(fluid.getBlock(), new StateMapperBase() {
      @Override
      protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return modelResourceLocation;
      }
    });
  }

  private static void registerBlockModels() {
    GradientBlocks.RegistrationHandler.ITEM_BLOCKS.stream()
      .filter(item -> !itemsRegistered.contains(item))
      .forEach(ModelManager::registerItemModel);
  }

  private static void registerItemModels() {
    // Register items with custom model names first
    //registerItemModel(ModItems.SNOWBALL_LAUNCHER, "minecraft:fishing_rod");

    //registerVariantItemModels(ModItems.VARIANTS_ITEM, "variant", ItemVariants.EnumType.values());

    // Then register items with default model names
    GradientItems.RegistrationHandler.ITEMS.stream()
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
      ModelLoader.setCustomModelResourceLocation(item, 0, fullModelLocation);
      return;
    }

    final NonNullList<ItemStack> stacks = NonNullList.create();
    item.getSubItems(item.getCreativeTab(), stacks);

    stacks.forEach(stack -> ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, item.getTranslationKey(stack).substring(5)), "inventory")));
  }

  @FunctionalInterface
  public interface CustomModel {
    void registerCustomModels();
  }
}
