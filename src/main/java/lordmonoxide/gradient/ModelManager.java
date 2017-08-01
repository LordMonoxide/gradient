package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GradientMod.MODID)
public final class ModelManager {
  private static final ModelManager instance = new ModelManager();
  
  private final Set<Item> itemsRegistered = new HashSet<>();
  
  private ModelManager() { }
  
  @SubscribeEvent
  public static void registerModels(final ModelRegistryEvent event) {
    System.out.println("Registering models");
    
    instance.registerFluidModels();
    instance.registerBlockModels();
    instance.registerItemModels();
  }
  
  private void registerFluidModels() {
    GradientMetals.instance.metals.forEach(metal -> this.registerFluidModel(metal.getFluid()));
  }
  
  private void registerFluidModel(final Fluid fluid) {
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
  
  private void registerBlockModels() {
    GradientBlocks.RegistrationHandler.ITEM_BLOCKS.stream().filter(item -> !this.itemsRegistered.contains(item)).forEach(this::registerItemModel);
  }
  
  private void registerItemModels() {
    // Register items with custom model names first
    //registerItemModel(ModItems.SNOWBALL_LAUNCHER, "minecraft:fishing_rod");
    
    //registerVariantItemModels(ModItems.VARIANTS_ITEM, "variant", ItemVariants.EnumType.values());
    
    // Then register items with default model names
    GradientItems.RegistrationHandler.ITEMS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(this::registerItemModel);
  }
  
  private void registerItemModel(final Item item) {
    this.registerItemModel(item, item.getRegistryName().toString());
  }
  
  private void registerItemModel(final Item item, final String modelLocation) {
    final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
    this.registerItemModel(item, fullModelLocation);
  }
  
  private void registerItemModel(final Item item, final ModelResourceLocation fullModelLocation) {
    this.itemsRegistered.add(item);
  
    if(!item.getHasSubtypes()) {
      ModelBakery.registerItemVariants(item, fullModelLocation);
      ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> fullModelLocation));
    } else {
      NonNullList<ItemStack> stacks = NonNullList.create();
      item.getSubItems(item, null, stacks);
      
      if(item instanceof ItemBlock) {
        for(ItemStack stack : stacks) {
          ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
      } else {
        for(ItemStack stack : stacks) {
          ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, item.getUnlocalizedName(stack).substring(5)), "inventory"));
        }
      }
    }
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
}