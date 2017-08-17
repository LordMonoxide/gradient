package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.armour.ClothPants;
import lordmonoxide.gradient.items.armour.ClothShirt;
import lordmonoxide.gradient.items.armour.GradientArmour;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GradientItems {
  private GradientItems() { }
  
  public static final ItemArmor.ArmorMaterial MATERIAL_CLOTH = EnumHelper.addArmorMaterial("cloth", "cloth", 5, new int[] {0, 1, 2, 0}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
  
  public static final GradientItem INFINICOAL = RegistrationHandler.register(new Infinicoal());
  public static final GradientItem DEBUG      = RegistrationHandler.register(new DebugItem());
  
  public static final GradientItem FIBRE = RegistrationHandler.register(new Fibre());
  public static final GradientItem CLOTH = RegistrationHandler.register(new Cloth());
  
  public static final GradientItemTool BONE_NEEDLE = RegistrationHandler.register(new BoneNeedle());
  
  public static final GradientItem     FIRE_STARTER  = RegistrationHandler.register(new FireStarter());
  public static final GradientItemTool STONE_HAMMER  = RegistrationHandler.register(new StoneHammer());
  public static final GradientItemTool STONE_MATTOCK = RegistrationHandler.register(new StoneMattock());
  public static final GradientItemTool FLINT_KNIFE   = RegistrationHandler.register(new FlintKnife());
  
  public static final GradientArmour CLOTH_SHIRT = RegistrationHandler.register(new ClothShirt());
  public static final GradientArmour CLOTH_PANTS = RegistrationHandler.register(new ClothPants());
  
  public static final GradientItem INGOT  = RegistrationHandler.register(new Ingot());
  public static final GradientItem NUGGET = RegistrationHandler.register(new Nugget());
  public static final GradientItem DUST   = RegistrationHandler.register(new Dust());
  public static final GradientItem PLATE  = RegistrationHandler.register(new Plate());
  
  public static final GradientItem CAST_ITEM = RegistrationHandler.register(new CastItem());
  public static final GradientItem TOOL      = RegistrationHandler.register(new Tool());
  
  public static final GradientItem BRONZE_MACHINE_HULL = RegistrationHandler.register(new BronzeMachineHull());
  
  private static void initialiseItems() {
    MATERIAL_CLOTH.setRepairItem(CLOTH.getItemStack());
    
    OreDictionary.registerOre("oreMagnesium", GradientBlocks.ORE_MAGNESIUM);
    
    OreDictionary.registerOre("infinicoal", INFINICOAL);
    
    OreDictionary.registerOre("coal", Items.COAL);
    
    OreDictionary.registerOre("string", FIBRE);
    OreDictionary.registerOre("cloth",  CLOTH);
    
    OreDictionary.registerOre("needle", BONE_NEEDLE.getWildcardItemStack());
    OreDictionary.registerOre("toolHammer", STONE_HAMMER.getWildcardItemStack());
    OreDictionary.registerOre("toolMattock", STONE_MATTOCK.getWildcardItemStack());
    
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      String caps = StringUtils.capitalize(metal.name);
      
      OreDictionary.registerOre("ingot" + caps, Ingot.getIngot(metal));
      OreDictionary.registerOre("nugget" + caps, Nugget.getNugget(metal));
      OreDictionary.registerOre("dust" + caps, Dust.getDust(metal));
    }
    
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
    private static final List<GradientCraftable> craftables = new ArrayList<>();
    
    public static final Set<Item> ITEMS = new HashSet<>();
    
    private static <T extends Item> T register(final T item) {
      ITEMS.add(item);
      
      if(item instanceof GradientCraftable) {
        craftables.add((GradientCraftable)item);
      }
      
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
        ITEMS.add(item);
      }
      
      initialiseItems();
    }
    
    public static void addRecipes() {
      craftables.forEach(GradientCraftable::addRecipe);
    }
  }
}
