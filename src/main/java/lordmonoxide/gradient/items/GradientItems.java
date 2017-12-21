package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.armour.ClothPants;
import lordmonoxide.gradient.items.armour.ClothShirt;
import lordmonoxide.gradient.items.armour.GradientArmour;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class GradientItems {
  private GradientItems() { }
  
  @Nonnull
  public static final ItemArmor.ArmorMaterial MATERIAL_CLOTH = EnumHelper.addArmorMaterial("cloth", "cloth", 5, new int[] {0, 1, 2, 0}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
  
  public static final GradientItem INFINICOAL = RegistrationHandler.register(new Infinicoal());
  public static final GradientItem DEBUG      = RegistrationHandler.register(new DebugItem());
  
  public static final GradientItem FIBRE = RegistrationHandler.register(new Fibre());
  public static final GradientItem CLOTH = RegistrationHandler.register(new Cloth());
  
  public static final GradientItemTool BONE_NEEDLE = RegistrationHandler.register(new BoneNeedle());
  
  public static final GradientItem          FIRE_STARTER  = RegistrationHandler.register(new FireStarter());
  public static final GradientItemWorldTool STONE_HAMMER  = RegistrationHandler.register(new StoneHammer());
  public static final GradientItemWorldTool STONE_MATTOCK = RegistrationHandler.register(new StoneMattock());
  public static final GradientItemWorldTool FLINT_KNIFE   = RegistrationHandler.register(new FlintKnife());
  
  public static final GradientArmour CLOTH_SHIRT = RegistrationHandler.register(new ClothShirt());
  public static final GradientArmour CLOTH_PANTS = RegistrationHandler.register(new ClothPants());
  
  public static final GradientItem DUST_FLINT = RegistrationHandler.register(new DustFlint());
  
  public static final GradientItemTool MORTAR = RegistrationHandler.register(new Mortar());
  
  public static final MushroomStew MUSHROOM_STEW = RegistrationHandler.register(new MushroomStew());
  
  static {
    // Register nuggets
    GradientMetals.metals.stream().filter(metal -> metal.canMakeNuggets).map(Nugget::new).forEach(RegistrationHandler::register);
    
    // Register dusts
    GradientMetals.metals.stream().map(Dust::new).forEach(RegistrationHandler::register);
    
    // Register cast items
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if((cast.isValidForMetal(metal)) && cast.itemForMetal(metal) == null) {
          RegistrationHandler.register(new CastItem(cast, metal));
        }
      }
    }
    
    // Register plates
    GradientMetals.metals.stream().filter(metal -> metal.canMakePlates).map(Plate::new).forEach(RegistrationHandler::register);
    
    // Register tools
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          RegistrationHandler.register(new Tool(type, metal));
        }
      }
    }
  }
  
  public static final GradientItem IGNITER = RegistrationHandler.register(new Igniter());
  
  private static void initialiseItems() {
    MATERIAL_CLOTH.setRepairItem(CLOTH.getItemStack());
  }
  
  private static void initialiseOreDict() {
    OreDictionary.registerOre("oreMagnesium", GradientBlocks.ORE_MAGNESIUM);
    
    OreDictionary.registerOre("infinicoal", INFINICOAL);
    
    OreDictionary.registerOre("coal", Items.COAL);
    OreDictionary.registerOre("igniter", GradientItems.IGNITER);
    
    OreDictionary.registerOre("string", FIBRE);
    OreDictionary.registerOre("cloth",  CLOTH);
    
    OreDictionary.registerOre("needle", BONE_NEEDLE.getWildcardItemStack());
    OreDictionary.registerOre("toolHammer", STONE_HAMMER.getWildcardItemStack());
    OreDictionary.registerOre("toolMattock", STONE_MATTOCK.getWildcardItemStack());
    
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String caps = StringUtils.capitalize(metal.name);
      
      if(metal.canMakeNuggets) {
        OreDictionary.registerOre("nugget" + caps, Nugget.getNugget(metal, 1));
      }
      
      OreDictionary.registerOre("dust" + caps, Dust.getDust(metal, 1));
      
      if(metal.canMakeIngots) {
        OreDictionary.registerOre("ingot" + caps, CastItem.getCastItem(GradientCasts.INGOT, metal, 1));
      }
      
      OreDictionary.registerOre("block" + caps, CastItem.getCastItem(GradientCasts.BLOCK, metal, 1));
      
      if(metal.canMakePlates) {
        OreDictionary.registerOre("plate" + caps, Plate.getPlate(metal, 1));
      }
      
      if(metal.canMakeTools) {
        OreDictionary.registerOre("toolMattock", Tool.getTool(GradientTools.MATTOCK, metal, 1, OreDictionary.WILDCARD_VALUE));
      }
    }
    
    OreDictionary.registerOre("dustFlint", DUST_FLINT);
    
    Blocks.OAK_STAIRS.setHarvestLevel("axe", 0);
    Blocks.SPRUCE_STAIRS.setHarvestLevel("axe", 0);
    Blocks.BIRCH_STAIRS.setHarvestLevel("axe", 0);
    Blocks.JUNGLE_STAIRS.setHarvestLevel("axe", 0);
    Blocks.ACACIA_STAIRS.setHarvestLevel("axe", 0);
    Blocks.DARK_OAK_STAIRS.setHarvestLevel("axe", 0);
    
    Blocks.OAK_DOOR.setHarvestLevel("axe", 0);
    Blocks.SPRUCE_DOOR.setHarvestLevel("axe", 0);
    Blocks.BIRCH_DOOR.setHarvestLevel("axe", 0);
    Blocks.JUNGLE_DOOR.setHarvestLevel("axe", 0);
    Blocks.ACACIA_DOOR.setHarvestLevel("axe", 0);
    Blocks.DARK_OAK_DOOR.setHarvestLevel("axe", 0);
    
    Blocks.OAK_FENCE.setHarvestLevel("axe", 0);
    Blocks.SPRUCE_FENCE.setHarvestLevel("axe", 0);
    Blocks.BIRCH_FENCE.setHarvestLevel("axe", 0);
    Blocks.JUNGLE_FENCE.setHarvestLevel("axe", 0);
    Blocks.ACACIA_FENCE.setHarvestLevel("axe", 0);
    Blocks.DARK_OAK_FENCE.setHarvestLevel("axe", 0);
    
    Blocks.OAK_FENCE_GATE.setHarvestLevel("axe", 0);
    Blocks.SPRUCE_FENCE_GATE.setHarvestLevel("axe", 0);
    Blocks.BIRCH_FENCE_GATE.setHarvestLevel("axe", 0);
    Blocks.JUNGLE_FENCE_GATE.setHarvestLevel("axe", 0);
    Blocks.ACACIA_FENCE_GATE.setHarvestLevel("axe", 0);
    Blocks.DARK_OAK_FENCE_GATE.setHarvestLevel("axe", 0);
    
    Blocks.STONE_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.BRICK_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.STONE_BRICK_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.NETHER_BRICK_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.SANDSTONE_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.QUARTZ_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.RED_SANDSTONE_STAIRS.setHarvestLevel("pickaxe", 0);
    Blocks.PURPUR_STAIRS.setHarvestLevel("pickaxe", 0);
    
    Blocks.IRON_DOOR.setHarvestLevel("pickaxe", 2);
    
    Blocks.NETHER_BRICK_FENCE.setHarvestLevel("pickaxe", 0);
    Blocks.COBBLESTONE_WALL.setHarvestLevel("pickaxe", 0);
  }
  
  @Mod.EventBusSubscriber(modid = GradientMod.MODID)
  public static class RegistrationHandler {
    
    public static final List<Item> ITEMS = new ArrayList<>();
    
    private static <T extends Item> T register(final T item) {
      ITEMS.add(item);
      return item;
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
      System.out.println("Registering items");
      
      // Trigger item registration
      new GradientItems();
      
      final IForgeRegistry<Item> registry = event.getRegistry();
      
      for(final Item item : ITEMS) {
        registry.register(item);
      }
      
      initialiseItems();
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void initOreDict(final RegistryEvent.Register<Item> event) {
      System.out.println("Registering ore dict entries");
      
      initialiseOreDict();
    }
  }
}
