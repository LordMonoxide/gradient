package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.BlockMetalFluid;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.ItemBlockProvider;
import lordmonoxide.gradient.blocks.claybucket.ItemClayBucket;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.blocks.claycast.ItemClayCastUnhardened;
import lordmonoxide.gradient.blocks.kinetic.flywheel.ItemFlywheel;
import lordmonoxide.gradient.blocks.pebble.EntityPebble;
import lordmonoxide.gradient.blocks.pebble.ItemPebble;
import lordmonoxide.gradient.items.armour.GradientArmour;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientItems {
  private GradientItems() { }

  public static final List<Item> ITEMS = new ArrayList<>();

  public static final ItemArmor.ArmorMaterial MATERIAL_HIDE = EnumHelper.addArmorMaterial("hide", GradientMod.resource("hide").toString(), 3, new int[] {1, 1, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);

  public static final GradientItem INFINICOAL = new GradientItem("infinicoal", CreativeTabs.MATERIALS);
  public static final DebugItem    DEBUG      = new DebugItem();

  public static final GradientItem FIBRE = new GradientItem("fibre", CreativeTabs.MATERIALS);
  public static final GradientItem TWINE = new GradientItem("twine", CreativeTabs.MATERIALS);

  public static final GradientItem BARK_OAK      = new GradientItem("bark_oak", CreativeTabs.MATERIALS);
  public static final GradientItem BARK_SPRUCE   = new GradientItem("bark_spruce", CreativeTabs.MATERIALS);
  public static final GradientItem BARK_BIRCH    = new GradientItem("bark_birch", CreativeTabs.MATERIALS);
  public static final GradientItem BARK_JUNGLE   = new GradientItem("bark_jungle", CreativeTabs.MATERIALS);
  public static final GradientItem BARK_ACACIA   = new GradientItem("bark_acacia", CreativeTabs.MATERIALS);
  public static final GradientItem BARK_DARK_OAK = new GradientItem("bark_dark_oak", CreativeTabs.MATERIALS);

  public static final Mulch MULCH = new Mulch();

  public static final GradientItem HIDE_COW        = new GradientItem("hide_cow", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_DONKEY     = new GradientItem("hide_donkey", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_HORSE      = new GradientItem("hide_horse", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_LLAMA      = new GradientItem("hide_llama", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_MULE       = new GradientItem("hide_mule", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_OCELOT     = new GradientItem("hide_ocelot", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_PIG        = new GradientItem("hide_pig", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_POLAR_BEAR = new GradientItem("hide_polar_bear", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_SHEEP      = new GradientItem("hide_sheep", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_WOLF       = new GradientItem("hide_wolf", CreativeTabs.MATERIALS);

  public static final GradientItem HIDE_RAW       = new GradientItem("hide_raw", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_SALTED    = new GradientItem("hide_salted", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_PRESERVED = new GradientItem("hide_preserved", CreativeTabs.MATERIALS);
  public static final GradientItem HIDE_TANNED    = new GradientItem("hide_tanned", CreativeTabs.MATERIALS);

  public static final GradientArmour HIDE_BOOTS     = new GradientArmour("hide_boots", MATERIAL_HIDE, 0, EntityEquipmentSlot.FEET);
  public static final GradientArmour HIDE_PANTS     = new GradientArmour("hide_pants", MATERIAL_HIDE, 0, EntityEquipmentSlot.LEGS);
  public static final GradientArmour HIDE_SHIRT     = new GradientArmour("hide_shirt", MATERIAL_HIDE, 0, EntityEquipmentSlot.CHEST);
  public static final GradientArmour HIDE_HEADCOVER = new GradientArmour("hide_headcover", MATERIAL_HIDE, 0, EntityEquipmentSlot.HEAD);

  public static final HideBedding  HIDE_BEDDING = new HideBedding();
  public static final Waterskin    WATERSKIN    = new Waterskin();
  public static final GradientItem LEATHER_CORD = new GradientItem("leather_cord", CreativeTabs.MATERIALS);

  public static final FireStarter  FIRE_STARTER  = new FireStarter();
  public static final StoneHammer  STONE_HAMMER  = new StoneHammer();
  public static final StoneHatchet STONE_HATCHET = new StoneHatchet();
  public static final StoneMattock STONE_MATTOCK = new StoneMattock();
  public static final StonePickaxe STONE_PICKAXE = new StonePickaxe();
  public static final FlintKnife   FLINT_KNIFE   = new FlintKnife();
  public static final BoneAwl      BONE_AWL      = new BoneAwl();

  public static final GradientItem NUGGET_COAL = new GradientItem("nugget.coal", CreativeTabs.MATERIALS);
  public static final GradientItem DUST_FLINT  = new GradientItem("dust.flint", CreativeTabs.MATERIALS);

  public static final GradientItem HARDENED_STICK = new GradientItem("hardened_stick", CreativeTabs.MATERIALS);

  public static final ItemClayBucket CLAY_BUCKET = new ItemClayBucket();

  public static final GradientItem SUGARCANE_PASTE = new GradientItem("sugarcane_paste", CreativeTabs.FOOD);
  public static final GradientItem SALT = new GradientItem("salt", CreativeTabs.FOOD);
  public static final GradientItem FLOUR = new GradientItem("flour", CreativeTabs.FOOD);
  public static final GradientItem DOUGH = new GradientItem("dough", CreativeTabs.FOOD);

  public static final GradientItem WOODEN_GEAR = new GradientItem("wooden_gear", CreativeTabs.MATERIALS);

  private static final Map<GradientMetals.Metal, ItemMetal> NUGGET = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> CRUSHED = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> CRUSHED_PURIFIED = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> DUST = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> PLATE = new HashMap<>();
  private static final Map<GradientCasts.Cast, Map<GradientMetals.Metal, CastItem>> CAST_ITEM = new HashMap<>();
  private static final Map<GradientTools.Type, Map<GradientMetals.Metal, Tool>> TOOL = new HashMap<>();
  private static final Map<GradientMetals.Alloy, ItemMetal> ALLOY_NUGGET = new HashMap<>();

  static {
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(metal.canMakeNuggets) {
        NUGGET.put(metal, new ItemMetal("nugget", metal));
      }

      if(metal.canMakeIngots) {
        CRUSHED.put(metal, new ItemMetal("crushed", metal));
        CRUSHED_PURIFIED.put(metal, new ItemMetal("crushed.purified", metal));
      }

      DUST.put(metal, new ItemMetal("dust", metal));

      if(metal.canMakePlates) {
        PLATE.put(metal, new ItemMetal("plate", metal));
      }
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Map<GradientMetals.Metal, CastItem> castItems = new HashMap<>();

      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(cast.isValidForMetal(metal) && cast.itemForMetal(metal) == null) {
          castItems.put(metal, new CastItem(cast, metal));
        }
      }

      CAST_ITEM.put(cast, castItems);
    }

    for(final GradientTools.Type type : GradientTools.types()) {
      final Map<GradientMetals.Metal, Tool> tools = new HashMap<>();

      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          tools.put(metal, new Tool(type, metal));
        }
      }

      TOOL.put(type, tools);
    }

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      ALLOY_NUGGET.put(alloy, new ItemMetal("alloy_nugget", alloy.output.metal));
    }
  }

  public static final GradientItem IGNITER = new GradientItem("igniter", CreativeTabs.MATERIALS);

  public static final GradientItem GRINDING_HEAD = new GradientItem("grinding_head", CreativeTabs.MATERIALS);

  public static ItemMetal nugget(final GradientMetals.Metal metal) {
    return NUGGET.get(metal);
  }

  public static ItemMetal crushed(final GradientMetals.Metal metal) {
    return CRUSHED.get(metal);
  }

  public static ItemMetal crushedPurified(final GradientMetals.Metal metal) {
    return CRUSHED_PURIFIED.get(metal);
  }

  public static ItemMetal dust(final GradientMetals.Metal metal) {
    return DUST.get(metal);
  }

  public static ItemMetal plate(final GradientMetals.Metal metal) {
    return PLATE.get(metal);
  }

  public static ItemStack castItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal, final int amount) {
    final ItemStack stack = cast.itemForMetal(metal);

    if(stack != null) {
      return ItemHandlerHelper.copyStackWithSize(stack, amount);
    }

    return CAST_ITEM.get(cast).get(metal).getItemStack(amount);
  }

  public static Tool tool(final GradientTools.Type type, final GradientMetals.Metal metal) {
    return TOOL.get(type).get(metal);
  }

  public static ItemMetal alloyNugget(final GradientMetals.Alloy alloy) {
    return ALLOY_NUGGET.get(alloy);
  }

  @SubscribeEvent
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientMod.logger.info("Registering items");

    final RegistrationHelper registry = new RegistrationHelper(event.getRegistry());

    registry.register(new ItemBlock(GradientBlocks.STRIPPED_OAK_WOOD).setRegistryName(GradientBlocks.STRIPPED_OAK_WOOD.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.STRIPPED_SPRUCE_WOOD).setRegistryName(GradientBlocks.STRIPPED_SPRUCE_WOOD.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.STRIPPED_BIRCH_WOOD).setRegistryName(GradientBlocks.STRIPPED_BIRCH_WOOD.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.STRIPPED_JUNGLE_WOOD).setRegistryName(GradientBlocks.STRIPPED_JUNGLE_WOOD.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.STRIPPED_ACACIA_WOOD).setRegistryName(GradientBlocks.STRIPPED_ACACIA_WOOD.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.STRIPPED_DARK_OAK_WOOD).setRegistryName(GradientBlocks.STRIPPED_DARK_OAK_WOOD.getRegistryName()));

    registry.register(new ItemPebble(GradientBlocks.PEBBLE).setRegistryName(GradientBlocks.PEBBLE.getRegistryName()));
    registry.register(new ItemClayCastUnhardened(GradientBlocks.CLAY_CAST_UNHARDENED).setRegistryName(GradientBlocks.CLAY_CAST_UNHARDENED.getRegistryName()));
    registry.register(new ItemClayCast(GradientBlocks.CLAY_CAST_HARDENED).setRegistryName(GradientBlocks.CLAY_CAST_HARDENED.getRegistryName()));

    registry.register(new ItemFlywheel(GradientBlocks.FLYWHEEL).setRegistryName(GradientBlocks.FLYWHEEL.getRegistryName()));

    for(final Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
      if(block instanceof BlockMetalFluid || !block.getRegistryName().getNamespace().equals(GradientMod.MODID) || registry.has(block.getRegistryName())) {
        continue;
      }

      final Item item;

      if(block instanceof ItemBlockProvider) {
        item = ((ItemBlockProvider)block).getItemBlock((Block & ItemBlockProvider)block);
      } else {
        item = new ItemBlock(block);
      }

      registry.register(item.setRegistryName(block.getRegistryName()));
    }

    registry.register(INFINICOAL);
    registry.register(DEBUG);

    registry.register(FIBRE);
    registry.register(TWINE);

    registry.register(BARK_OAK);
    registry.register(BARK_SPRUCE);
    registry.register(BARK_BIRCH);
    registry.register(BARK_JUNGLE);
    registry.register(BARK_ACACIA);
    registry.register(BARK_DARK_OAK);

    registry.register(MULCH);

    registry.register(HIDE_COW);
    registry.register(HIDE_DONKEY);
    registry.register(HIDE_HORSE);
    registry.register(HIDE_LLAMA);
    registry.register(HIDE_MULE);
    registry.register(HIDE_OCELOT);
    registry.register(HIDE_PIG);
    registry.register(HIDE_POLAR_BEAR);
    registry.register(HIDE_SHEEP);
    registry.register(HIDE_WOLF);

    registry.register(HIDE_RAW);
    registry.register(HIDE_SALTED);
    registry.register(HIDE_PRESERVED);
    registry.register(HIDE_TANNED);

    registry.register(HIDE_BOOTS);
    registry.register(HIDE_PANTS);
    registry.register(HIDE_SHIRT);
    registry.register(HIDE_HEADCOVER);

    registry.register(HIDE_BEDDING);
    registry.register(WATERSKIN);
    registry.register(LEATHER_CORD);

    registry.register(FIRE_STARTER);
    registry.register(STONE_HAMMER);
    registry.register(STONE_HATCHET);
    registry.register(STONE_MATTOCK);
    registry.register(STONE_PICKAXE);
    registry.register(FLINT_KNIFE);
    registry.register(BONE_AWL);

    registry.register(NUGGET_COAL);
    registry.register(DUST_FLINT);

    registry.register(HARDENED_STICK);

    registry.register(CLAY_BUCKET);

    registry.register(SUGARCANE_PASTE);
    registry.register(SALT);
    registry.register(FLOUR);
    registry.register(DOUGH);

    registry.register(WOODEN_GEAR);

    registry.register(IGNITER);

    registry.register(GRINDING_HEAD);

    NUGGET.values().forEach(registry::register);
    CRUSHED.values().forEach(registry::register);
    CRUSHED_PURIFIED.values().forEach(registry::register);
    DUST.values().forEach(registry::register);
    PLATE.values().forEach(registry::register);
    CAST_ITEM.values().forEach(map -> map.values().forEach(registry::register));
    TOOL.values().forEach(map -> map.values().forEach(registry::register));
    ALLOY_NUGGET.values().forEach(registry::register);

    MinecraftForge.EVENT_BUS.register(CLAY_BUCKET);

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), new BehaviorProjectileDispense() {
      @Override
      protected IProjectile getProjectileEntity(final World world, final IPosition position, final ItemStack stack) {
        return new EntityPebble(world, position.getX(), position.getY(), position.getZ());
      }
    });
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void initOreDict(final RegistryEvent.Register<Item> event) {
    GradientMod.logger.info("Registering ore dict entries");

    OreDictionary.registerOre("coal", Items.COAL);

    OreDictionary.registerOre("fibre",  FIBRE);
    OreDictionary.registerOre("string", TWINE);
    OreDictionary.registerOre("string", LEATHER_CORD);

    OreDictionary.registerOre("barkWood", BARK_OAK);
    OreDictionary.registerOre("barkWood", BARK_SPRUCE);
    OreDictionary.registerOre("barkWood", BARK_BIRCH);
    OreDictionary.registerOre("barkWood", BARK_JUNGLE);
    OreDictionary.registerOre("barkWood", BARK_ACACIA);
    OreDictionary.registerOre("barkWood", BARK_DARK_OAK);

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
    OreDictionary.registerOre("awl", BONE_AWL.getWildcardItemStack());
    OreDictionary.registerOre("toolHammer", STONE_HAMMER.getWildcardItemStack());
    OreDictionary.registerOre("toolAxe", STONE_HATCHET.getWildcardItemStack());
    OreDictionary.registerOre("toolAxe", STONE_MATTOCK.getWildcardItemStack());
    OreDictionary.registerOre("toolHoe", STONE_MATTOCK.getWildcardItemStack());
    OreDictionary.registerOre("toolPickaxe", STONE_PICKAXE.getWildcardItemStack());
    OreDictionary.registerOre("toolKnife", FLINT_KNIFE.getWildcardItemStack());

    // Metals/metal tools
    OreDictionary.registerOre("oreMagnesium", GradientBlocks.ORE_MAGNESIUM);
    OreDictionary.registerOre("nuggetCoal", NUGGET_COAL);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String caps = StringUtils.capitalize(metal.name);

      if(metal.canMakeNuggets) {
        OreDictionary.registerOre("nugget" + caps, nugget(metal));
        OreDictionary.registerOre("crushed" + caps, crushed(metal));
        OreDictionary.registerOre("crushedPurified" + caps, crushedPurified(metal));
      }

      OreDictionary.registerOre("dust" + caps, dust(metal));

      if(metal.canMakeIngots) {
        OreDictionary.registerOre("ingot" + caps, GradientItems.castItem(GradientCasts.INGOT, metal, 1));
      }

      OreDictionary.registerOre("block" + caps, GradientItems.castItem(GradientCasts.BLOCK, metal, 1));

      if(metal.canMakePlates) {
        OreDictionary.registerOre("plate" + caps, plate(metal));
      }

      if(metal.canMakeTools) {
        final ItemStack stack = tool(GradientTools.MATTOCK, metal).getWildcardItemStack();
        OreDictionary.registerOre("toolAxe", stack);
        OreDictionary.registerOre("toolHoe", stack);
      }
    }

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      final String name = StringUtils.capitalize(alloy.output.metal.name);

      OreDictionary.registerOre("alloyNugget" + name, alloyNugget(alloy));
    }

    OreDictionary.registerOre("dustFlint", DUST_FLINT);

    // Crops/food
    OreDictionary.registerOre("ingredientFlour", FLOUR);
    OreDictionary.registerOre("ingredientSalt", SALT);
    OreDictionary.registerOre("ingredientSugar", Items.SUGAR);

    final Item naturaMaterials = Item.getByNameOrId("natura:materials");
    if(naturaMaterials != null) {
      OreDictionary.registerOre("cropWheat", new ItemStack(naturaMaterials, 1, 0)); // Barley
      OreDictionary.registerOre("ingredientFlour", new ItemStack(naturaMaterials, 1, 1)); // Barley flour
      OreDictionary.registerOre("ingredientFlour", new ItemStack(naturaMaterials, 1, 2)); // Wheat flour
    }

    final ItemStack clayBucketWater = ItemClayBucket.getFilledBucket(FluidRegistry.WATER);

    OreDictionary.registerOre("listAllwater", WATERSKIN.getFilled(FluidRegistry.WATER));
    OreDictionary.registerOre("listAllwater", clayBucketWater);

    // Buckets
    OreDictionary.registerOre("bucketWater", clayBucketWater);
    OreDictionary.registerOre("bucketLava",  ItemClayBucket.getFilledBucket(FluidRegistry.LAVA));
    OreDictionary.registerOre("bucketWater", FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME)));
    OreDictionary.registerOre("bucketLava",  FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.LAVA, Fluid.BUCKET_VOLUME)));

    for(final Fluid fluid : FluidRegistry.getBucketFluids()) {
      final String name = "bucket" + StringUtils.capitalize(fluid.getName());
      OreDictionary.registerOre(name, ItemClayBucket.getFilledBucket(fluid));
      OreDictionary.registerOre(name, FluidUtil.getFilledBucket(new FluidStack(fluid, Fluid.BUCKET_VOLUME)));
    }

    OreDictionary.registerOre("logWood", GradientBlocks.HARDENED_LOG);
    OreDictionary.registerOre("plankWood", GradientBlocks.HARDENED_PLANKS);
    OreDictionary.registerOre("stickWood", HARDENED_STICK);

    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_OAK_WOOD);
    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_SPRUCE_WOOD);
    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_BIRCH_WOOD);
    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_JUNGLE_WOOD);
    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_ACACIA_WOOD);
    OreDictionary.registerOre("logWood", GradientBlocks.STRIPPED_DARK_OAK_WOOD);

    OreDictionary.registerOre("axleWood", GradientBlocks.WOODEN_AXLE);
    OreDictionary.registerOre("gearWood", WOODEN_GEAR);

    Blocks.OAK_STAIRS.setHarvestLevel("axe", 0);
    Blocks.SPRUCE_STAIRS.setHarvestLevel("axe", 0);
    Blocks.BIRCH_STAIRS.setHarvestLevel("axe", 0);
    Blocks.JUNGLE_STAIRS.setHarvestLevel("axe", 0);
    Blocks.ACACIA_STAIRS.setHarvestLevel("axe", 0);
    Blocks.DARK_OAK_STAIRS.setHarvestLevel("axe", 0);

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

  private static final class RegistrationHelper {
    private final IForgeRegistry<Item> registry;

    public RegistrationHelper(final IForgeRegistry<Item> registry) {
      ITEMS.clear();
      this.registry = registry;
    }

    public void register(final Item item) {
      ITEMS.add(item);
      this.registry.register(item);
    }

    public boolean has(final ResourceLocation loc) {
      return this.registry.getValue(loc) != null;
    }
  }
}
