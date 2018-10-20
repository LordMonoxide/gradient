package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.claybucket.ItemClayBucket;
import lordmonoxide.gradient.items.armour.GradientArmour;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class GradientItems {
  private GradientItems() { }

  public static final ItemArmor.ArmorMaterial MATERIAL_HIDE = EnumHelper.addArmorMaterial("hide", GradientMod.resource("hide").toString(), 3, new int[] {1, 1, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);

  public static final GradientItem INFINICOAL = RegistrationHandler.register(new Infinicoal());
  public static final GradientItem DEBUG      = RegistrationHandler.register(new DebugItem());

  public static final GradientItem FIBRE = RegistrationHandler.register(new Fibre());
  public static final GradientItem TWINE = RegistrationHandler.register(new Twine());

  private static Item HIDE_COW;
  private static Item HIDE_DONKEY;
  private static Item HIDE_HORSE;
  private static Item HIDE_LLAMA;
  private static Item HIDE_MULE;
  private static Item HIDE_OCELOT;
  private static Item HIDE_PIG;
  private static Item HIDE_POLAR_BEAR;
  private static Item HIDE_SHEEP;
  private static Item HIDE_WOLF;

  public static final NuggetCoal NUGGET_COAL = RegistrationHandler.register(new NuggetCoal());

  public static final GradientItemTool BONE_AWL = RegistrationHandler.register(new BoneAwl());

  public static final GradientItem          FIRE_STARTER  = RegistrationHandler.register(new FireStarter());
  public static final GradientItemWorldTool STONE_HAMMER  = RegistrationHandler.register(new StoneHammer());
  public static final GradientItemWorldTool STONE_MATTOCK = RegistrationHandler.register(new StoneMattock());
  public static final GradientItemWorldTool FLINT_KNIFE   = RegistrationHandler.register(new FlintKnife());

  public static final GradientItem DUST_FLINT = RegistrationHandler.register(new DustFlint());

  public static final GradientItemTool MORTAR = RegistrationHandler.register(new Mortar());

  public static final MushroomStew MUSHROOM_STEW = RegistrationHandler.register(new MushroomStew());
  public static final SugarcanePaste SUGARCANE_PASTE = RegistrationHandler.register(new SugarcanePaste());
  public static final Salt SALT = RegistrationHandler.register(new Salt());
  public static final Flour FLOUR = RegistrationHandler.register(new Flour());
  public static final Dough DOUGH = RegistrationHandler.register(new Dough());

  static {
    // Register nuggets
    GradientMetals.metals.stream().filter(metal -> metal.canMakeNuggets).map(Nugget::new).forEach(RegistrationHandler::register);

    // Register crushed
    GradientMetals.metals.stream().filter(metal -> metal.canMakeIngots).map(Crushed::new).forEach(RegistrationHandler::register);
    GradientMetals.metals.stream().filter(metal -> metal.canMakeIngots).map(CrushedPurified::new).forEach(RegistrationHandler::register);

    // Register dusts
    GradientMetals.metals.stream().map(Dust::new).forEach(RegistrationHandler::register);

    // Register cast items
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(cast.isValidForMetal(metal) && cast.itemForMetal(metal) == null) {
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

    // Register alloy nuggets
    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      RegistrationHandler.register(new AlloyNugget(alloy.output.metal));
    }
  }

  public static final GradientItem IGNITER = RegistrationHandler.register(new Igniter());

  public static final GrindingHead GRINDING_HEAD = RegistrationHandler.register(new GrindingHead());

  public static final ItemClayBucket CLAY_BUCKET = RegistrationHandler.register(new ItemClayBucket());

  private static void initialiseItems() {
    MinecraftForge.EVENT_BUS.register(CLAY_BUCKET);
  }

  private static void initialiseOreDict() {
    OreDictionary.registerOre("infinicoal", INFINICOAL);

    OreDictionary.registerOre("coal", Items.COAL);

    OreDictionary.registerOre("fibre",  FIBRE);
    OreDictionary.registerOre("string", TWINE);

    OreDictionary.registerOre("hide", HIDE_COW);
    OreDictionary.registerOre("hide", HIDE_DONKEY);
    OreDictionary.registerOre("hide", HIDE_HORSE);
    OreDictionary.registerOre("hide", HIDE_LLAMA);
    OreDictionary.registerOre("hide", HIDE_MULE);
    OreDictionary.registerOre("hide", HIDE_OCELOT);
    OreDictionary.registerOre("hide", HIDE_PIG);
    OreDictionary.registerOre("hide", HIDE_POLAR_BEAR);
    OreDictionary.registerOre("hide", HIDE_SHEEP);
    OreDictionary.registerOre("hide", HIDE_WOLF);

    // Tools
    OreDictionary.registerOre("igniter", GradientItems.IGNITER);
    OreDictionary.registerOre("awl", BONE_AWL.getWildcardItemStack());
    OreDictionary.registerOre("toolHammer", STONE_HAMMER.getWildcardItemStack());
    OreDictionary.registerOre("toolMattock", STONE_MATTOCK.getWildcardItemStack());
    OreDictionary.registerOre("toolMortar", MORTAR.getWildcardItemStack());

    // Metals/metal tools
    OreDictionary.registerOre("oreMagnesium", GradientBlocks.ORE_MAGNESIUM);
    OreDictionary.registerOre("nuggetCoal", NUGGET_COAL);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String caps = StringUtils.capitalize(metal.name);

      if(metal.canMakeNuggets) {
        OreDictionary.registerOre("nugget" + caps, Nugget.get(metal, 1));
        OreDictionary.registerOre("crushed" + caps, Crushed.get(metal, 1));
        OreDictionary.registerOre("crushedPurified" + caps, CrushedPurified.get(metal, 1));
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

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      final String name = StringUtils.capitalize(alloy.output.metal.name);

      OreDictionary.registerOre("alloyNugget" + name, AlloyNugget.get(alloy.output.metal));
    }

    OreDictionary.registerOre("dustFlint", DUST_FLINT);

    // Crops/food
    OreDictionary.registerOre("ingredientFlour", GradientItems.FLOUR);
    OreDictionary.registerOre("ingredientSalt", GradientItems.SALT);
    OreDictionary.registerOre("ingredientSugar", Items.SUGAR);

    final Item naturaMaterials = Item.getByNameOrId("natura:materials");
    if(naturaMaterials != null) {
      OreDictionary.registerOre("cropWheat", new ItemStack(naturaMaterials, 1, 0)); // Barley
      OreDictionary.registerOre("ingredientFlour", new ItemStack(naturaMaterials, 1, 1)); // Barley flour
      OreDictionary.registerOre("ingredientFlour", new ItemStack(naturaMaterials, 1, 2)); // Wheat flour
    }

    // Buckets
    OreDictionary.registerOre("bucketWater", ItemClayBucket.getFilledBucket(FluidRegistry.WATER));
    OreDictionary.registerOre("bucketLava",  ItemClayBucket.getFilledBucket(FluidRegistry.LAVA));
    OreDictionary.registerOre("bucketWater", FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME)));
    OreDictionary.registerOre("bucketLava",  FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME)));

    for(final Fluid fluid : FluidRegistry.getBucketFluids()) {
      final String name = "bucket" + StringUtils.capitalize(fluid.getName());
      OreDictionary.registerOre(name, ItemClayBucket.getFilledBucket(fluid));
      OreDictionary.registerOre(name, FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)));
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
    public static final List<Item> ITEMS = new ArrayList<>();

    private RegistrationHandler() { }

    private static <T extends Item> T register(final T item) {
      ITEMS.add(item);
      return item;
    }

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
      GradientMod.logger.info("Registering items");

      HIDE_COW = RegistrationHandler.register(new Hide("hide_cow"));
      HIDE_DONKEY = RegistrationHandler.register(new Hide("hide_donkey"));
      HIDE_HORSE = RegistrationHandler.register(new Hide("hide_horse"));
      HIDE_LLAMA = RegistrationHandler.register(new Hide("hide_llama"));
      HIDE_MULE = RegistrationHandler.register(new Hide("hide_mule"));
      HIDE_OCELOT = RegistrationHandler.register(new Hide("hide_ocelot"));
      HIDE_PIG = RegistrationHandler.register(new Hide("hide_pig"));
      HIDE_POLAR_BEAR = RegistrationHandler.register(new Hide("hide_polar_bear"));
      HIDE_SHEEP = RegistrationHandler.register(new Hide("hide_sheep"));
      HIDE_WOLF = RegistrationHandler.register(new Hide("hide_wolf"));

      RegistrationHandler.register(new GradientArmour("hide_boots", MATERIAL_HIDE, 0, EntityEquipmentSlot.FEET));
      RegistrationHandler.register(new GradientArmour("hide_pants", MATERIAL_HIDE, 0, EntityEquipmentSlot.LEGS));
      RegistrationHandler.register(new GradientArmour("hide_shirt", MATERIAL_HIDE, 0, EntityEquipmentSlot.CHEST));
      RegistrationHandler.register(new GradientArmour("hide_headcover", MATERIAL_HIDE, 0, EntityEquipmentSlot.HEAD));

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
      GradientMod.logger.info("Registering ore dict entries");

      initialiseOreDict();
    }
  }
}
