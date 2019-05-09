package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientIds;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.entities.EntityPebble;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import lordmonoxide.gradient.utils.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(GradientMod.MODID)
public final class GradientItems {
  private GradientItems() { }

  public static final Item SALT_BLOCK = null;
  public static final Item PEBBLE = null;

  private static final Map<Metal, Item> METAL_PEBBLES = new LinkedHashMap<>();

  public static final Item FIBRE = null;
  public static final Item TWINE = null;

  public static final Item BARK_OAK = null;
  public static final Item BARK_SPRUCE = null;
  public static final Item BARK_BIRCH = null;
  public static final Item BARK_JUNGLE = null;
  public static final Item BARK_ACACIA = null;
  public static final Item BARK_DARK_OAK = null;

  public static final Mulch MULCH = null;

  public static final Item HIDE_COW = null;
  public static final Item HIDE_DONKEY = null;
  public static final Item HIDE_HORSE = null;
  public static final Item HIDE_LLAMA = null;
  public static final Item HIDE_MULE = null;
  public static final Item HIDE_OCELOT = null;
  public static final Item HIDE_PIG = null;
  public static final Item HIDE_POLAR_BEAR = null;
  public static final Item HIDE_SHEEP = null;
  public static final Item HIDE_WOLF = null;

  public static final Item HIDE_RAW = null;
  public static final Item HIDE_SALTED = null;
  public static final Item HIDE_PRESERVED = null;
  public static final Item HIDE_TANNED = null;

  public static final Item LEATHER_CORD = null;

  public static final HideBedding HIDE_BEDDING = null;
  public static final Waterskin WATERSKIN = null;

  public static final Item TORCH_STAND = null;
  public static final Item FIBRE_TORCH_UNLIT = null;
  public static final Item FIBRE_TORCH_LIT = null;
  public static final Item FIRE_PIT = null;
  public static final Item BELLOWS = null;
  public static final Item FIRE_STARTER = null;
  public static final Item IGNITER = null;

  public static final Item GRINDSTONE = null;
  public static final Item MIXING_BASIN = null;
  public static final Item DRYING_RACK = null;

  public static final Item CLAY_FURNACE_UNHARDENED = null;
  public static final Item CLAY_FURNACE_HARDENED = null;
  public static final Item CLAY_CRUCIBLE_UNHARDENED = null;
  public static final Item CLAY_CRUCIBLE_HARDENED = null;
  public static final Item CLAY_OVEN_UNHARDENED = null;
  public static final Item CLAY_OVEN_HARDENED = null;

  private static final Map<GradientCasts.Cast, Item> CLAY_CAST_UNHARDENED = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Item> CLAY_CAST_HARDENED = new LinkedHashMap<>();

  public static final Item CLAY_BUCKET_UNHARDENED = null;
  public static final Item CLAY_BUCKET_HARDENED = null;

  public static final Item HARDENED_LOG = null;
  public static final Item HARDENED_PLANKS = null;
  public static final Item HARDENED_STICK = null;

  public static final Item WOODEN_GEAR = null;
  public static final Item WOODEN_AXLE = null;
  public static final Item WOODEN_GEARBOX = null;
  public static final Item HAND_CRANK = null;
  public static final Item FLYWHEEL = null;

  public static final Item GRINDING_HEAD = null;
  public static final Item BRONZE_MACHINE_HULL = null;
  public static final Item BRONZE_FURNACE = null;
  public static final Item BRONZE_BOILER = null;
  public static final Item BRONZE_OVEN = null;
  public static final Item BRONZE_GRINDER = null;

  public static final Item SUGARCANE_PASTE = null;
  public static final Item SALT = null;
  public static final Item FLOUR = null;
  public static final Item DOUGH = null;

  public static final GradientArmour HIDE_BOOTS = null;
  public static final GradientArmour HIDE_PANTS = null;
  public static final GradientArmour HIDE_SHIRT = null;
  public static final GradientArmour HIDE_HEADCOVER = null;

  public static final StoneHammer STONE_HAMMER = null;
  public static final StoneHatchet STONE_HATCHET = null;
  public static final StoneMattock STONE_MATTOCK = null;
  public static final StonePickaxe STONE_PICKAXE = null;
  public static final FlintKnife FLINT_KNIFE = null;
  public static final BoneAwl BONE_AWL = null;
  private static final Map<GradientTools.Type, Map<Metal, Item>> TOOL = new LinkedHashMap<>();

  private static final Map<Ore.Metal, Item> ORE = new LinkedHashMap<>();
  private static final Map<Ore.Metal, Item> CRUSHED = new LinkedHashMap<>();
  private static final Map<Ore.Metal, Item> PURIFIED = new LinkedHashMap<>();
  private static final Map<Metal, Item> DUST = new LinkedHashMap<>();
  public static final Item DUST_FLINT = null;
  private static final Map<Metal, Item> NUGGET = new LinkedHashMap<>();
  public static final Item NUGGET_COAL = null;
  private static final Map<Metal, Item> PLATE = new LinkedHashMap<>();
  private static final Map<Metal, Item> ALLOY_NUGGET = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Map<Metal, Item>> CAST_ITEM = new LinkedHashMap<>();
  private static final Map<Metal, Item> CAST_BLOCK = new LinkedHashMap<>();

  public static final Item INFINICOAL = null;
  public static final DebugItem DEBUG = null;

  public static final ItemClayBucket CLAY_BUCKET = null;

  public static Item pebble(final Metal metal) {
    return METAL_PEBBLES.get(metal);
  }

  public static Item clayCastUnhardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_UNHARDENED.get(cast);
  }

  public static Item clayCastHardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_HARDENED.get(cast);
  }

  public static Item crushed(final Ore.Metal metal) {
    return CRUSHED.get(metal);
  }

  public static Item purified(final Ore.Metal metal) {
    return PURIFIED.get(metal);
  }

  public static Item nugget(final Metal metal) {
    return NUGGET.get(metal);
  }

  public static Item dust(final Metal metal) {
    return DUST.get(metal);
  }

  public static Item plate(final Metal metal) {
    return PLATE.get(metal);
  }

  public static ItemStack castItem(final GradientCasts.Cast cast, final Metal metal, final int amount) {
    final ItemStack stack = cast.itemForMetal(metal);

    if(stack != null) {
      return ItemHandlerHelper.copyStackWithSize(stack, amount);
    }

    return new ItemStack(CAST_ITEM.get(cast).get(metal), amount);
  }

  public static Item tool(final GradientTools.Type type, final Metal metal) {
    return TOOL.get(type).get(metal);
  }

  public static Item alloyNugget(final Metal metal) {
    return ALLOY_NUGGET.get(metal);
  }

  @SubscribeEvent
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientMod.logger.info("Registering items");

    final RegistryHelper<Item> registry = new RegistryHelper<>(event.getRegistry());

    registry.register(new ItemBlock(GradientBlocks.SALT_BLOCK, new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.SALT_BLOCK);
    final Item pebble = registry.register(new ItemPebble(GradientBlocks.PEBBLE), GradientIds.PEBBLE);

    for(final Metal metal : Metals.all()) {
      METAL_PEBBLES.put(metal, registry.register(new ItemPebble(GradientBlocks.pebble(metal)), GradientIds.pebble(metal)));
    }

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.FIBRE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.TWINE);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_OAK);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_SPRUCE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_BIRCH);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_JUNGLE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_ACACIA);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BARK_DARK_OAK);

    registry.register(new Mulch(), GradientIds.MULCH);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_COW);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_DONKEY);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_HORSE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_LLAMA);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_MULE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_OCELOT);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_PIG);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_POLAR_BEAR);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_SHEEP);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_WOLF);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_RAW);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_SALTED);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_PRESERVED);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HIDE_TANNED);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.LEATHER_CORD);

    registry.register(new HideBedding(), GradientIds.HIDE_BEDDING);
    registry.register(new Waterskin(), GradientIds.WATERSKIN);

    registry.register(new ItemBlock(GradientBlocks.TORCH_STAND, new Item.Properties().group(ItemGroup.DECORATIONS)), GradientIds.TORCH_STAND);
    registry.register(new ItemBlock(GradientBlocks.FIBRE_TORCH_UNLIT, new Item.Properties().group(ItemGroup.DECORATIONS)), GradientIds.FIBRE_TORCH_UNLIT);
    registry.register(new ItemBlock(GradientBlocks.FIBRE_TORCH_LIT, new Item.Properties().group(ItemGroup.DECORATIONS)), GradientIds.FIBRE_TORCH_LIT);
    registry.register(new ItemBlock(GradientBlocks.FIRE_PIT, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.FIRE_PIT);
    registry.register(new ItemBlock(GradientBlocks.BELLOWS, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.BELLOWS);
    registry.register(new FireStarter(), GradientIds.FIRE_STARTER);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.IGNITION_POWDER);

    registry.register(new ItemBlock(GradientBlocks.GRINDSTONE, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.GRINDSTONE);
    registry.register(new ItemBlock(GradientBlocks.MIXING_BASIN, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.MIXING_BASIN);
    registry.register(new ItemBlock(GradientBlocks.DRYING_RACK, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.DRYING_RACK);

    registry.register(new ItemBlock(GradientBlocks.CLAY_FURNACE_UNHARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_FURNACE_UNHARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_FURNACE_HARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_FURNACE_HARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_UNHARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_CRUCIBLE_UNHARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_HARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_CRUCIBLE_HARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_OVEN_UNHARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_OVEN_UNHARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_OVEN_HARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_OVEN_HARDENED);

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CAST_UNHARDENED.put(cast, registry.register(new ItemClayCastUnhardened(GradientBlocks.clayCastUnhardened(cast), cast, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.clayCastUnhardened(cast)));
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CAST_HARDENED.put(cast, registry.register(new ItemBlock(GradientBlocks.clayCastHardened(cast), new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.clayCastHardened(cast)));
    }

    registry.register(new ItemBlock(GradientBlocks.CLAY_BUCKET_UNHARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_BUCKET_UNHARDENED);
    registry.register(new ItemBlock(GradientBlocks.CLAY_BUCKET_HARDENED, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.CLAY_BUCKET_HARDENED);

    registry.register(new ItemBlock(GradientBlocks.HARDENED_LOG, new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HARDENED_LOG);
    registry.register(new ItemBlock(GradientBlocks.HARDENED_PLANKS, new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HARDENED_PLANKS);
    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.HARDENED_STICK);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.WOODEN_GEAR);
    registry.register(new ItemBlock(GradientBlocks.WOODEN_AXLE, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.WOODEN_AXLE);
    registry.register(new ItemBlock(GradientBlocks.WOODEN_GEARBOX, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.WOODEN_GEARBOX);
    registry.register(new ItemBlock(GradientBlocks.HAND_CRANK, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.HAND_CRANK);
    registry.register(new ItemFlywheel(GradientBlocks.FLYWHEEL), GradientIds.FLYWHEEL);

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.GRINDING_HEAD);
    registry.register(new ItemBlock(GradientBlocks.BRONZE_MACHINE_HULL, new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.BRONZE_MACHINE_HULL);
    registry.register(new ItemBlock(GradientBlocks.BRONZE_FURNACE, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.BRONZE_FURNACE);
    registry.register(new ItemBlock(GradientBlocks.BRONZE_BOILER, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.BRONZE_BOILER);
    registry.register(new ItemBlock(GradientBlocks.BRONZE_OVEN, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.BRONZE_OVEN);
    registry.register(new ItemBlock(GradientBlocks.BRONZE_GRINDER, new Item.Properties().group(ItemGroup.TOOLS)), GradientIds.BRONZE_GRINDER);

    registry.register(new Item(new Item.Properties().group(ItemGroup.FOOD)), GradientIds.SUGARCANE_PASTE);
    registry.register(new Item(new Item.Properties().group(ItemGroup.FOOD)), GradientIds.SALT);
    registry.register(new Item(new Item.Properties().group(ItemGroup.FOOD)), GradientIds.FLOUR);
    registry.register(new Item(new Item.Properties().group(ItemGroup.FOOD)), GradientIds.DOUGH);

    registry.register(new GradientArmour(GradientArmourMaterial.HIDE, EntityEquipmentSlot.FEET, new Item.Properties()), GradientIds.HIDE_BOOTS);
    registry.register(new GradientArmour(GradientArmourMaterial.HIDE, EntityEquipmentSlot.LEGS, new Item.Properties()), GradientIds.HIDE_PANTS);
    registry.register(new GradientArmour(GradientArmourMaterial.HIDE, EntityEquipmentSlot.CHEST, new Item.Properties()), GradientIds.HIDE_SHIRT);
    registry.register(new GradientArmour(GradientArmourMaterial.HIDE, EntityEquipmentSlot.HEAD, new Item.Properties()), GradientIds.HIDE_HEADCOVER);

    registry.register(new StoneHammer(), GradientIds.STONE_HAMMER);
    registry.register(new StoneHatchet(), GradientIds.STONE_HATCHET);
    registry.register(new StoneMattock(), GradientIds.STONE_MATTOCK);
    registry.register(new StonePickaxe(), GradientIds.STONE_PICKAXE);
    registry.register(new FlintKnife(), GradientIds.FLINT_KNIFE);
    registry.register(new BoneAwl(), GradientIds.BONE_AWL);

    for(final GradientTools.Type type : GradientTools.types()) {
      final Map<Metal, Item> tools = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        if(metal.canMakeTools) {
          tools.put(metal, registry.register(new Tool(type, metal), GradientIds.tool(type, metal)));
        }
      }

      TOOL.put(type, tools);
    }

    for(final Ore.Metal ore : Ores.metals()) {
      ORE.put(ore, registry.register(new ItemBlock(GradientBlocks.ore(ore), new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.ore(ore)));
    }

    for(final Ore.Metal ore : Ores.metals()) {
      CRUSHED.put(ore, registry.register(new ItemMetal(ore.metal), GradientIds.crushed(ore)));
    }

    for(final Ore.Metal ore : Ores.metals()) {
      PURIFIED.put(ore, registry.register(new ItemMetal(ore.metal), GradientIds.purified(ore)));
    }

    for(final Metal metal : Metals.all()) {
      DUST.put(metal, registry.register(new ItemMetal(metal), GradientIds.dust(metal)));
    }

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.DUST_FLINT);

    for(final Metal metal : Metals.all()) {
      if(metal.canMakeIngots) {
        NUGGET.put(metal, registry.register(new ItemMetal(metal), GradientIds.nugget(metal)));
      }
    }

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.NUGGET_COAL);

    for(final Metal metal : Metals.all()) {
      if(metal.canMakeIngots) {
        if(metal.elements.size() > 1) {
          boolean make = true;

          for(final Metal.MetalElement element : metal.elements) {
            if(Metals.get(element.element) == Metals.INVALID_METAL) {
              make = false;
              break;
            }
          }

          if(make) {
            ALLOY_NUGGET.put(metal, registry.register(new ItemMetal(metal), GradientIds.alloyNugget(metal)));
          }
        }
      }
    }

    for(final Metal metal : Metals.all()) {
      if(metal.canMakeIngots && metal.canMakePlates) {
        PLATE.put(metal, registry.register(new ItemMetal(metal), GradientIds.plate(metal)));
      }
    }

    CAST_BLOCK.put(Metals.GLASS, Blocks.GLASS.asItem());

    for(final Metal metal : Metals.all()) {
      if(!CAST_BLOCK.containsKey(metal)) {
        final Block castBlock = GradientBlocks.castBlock(metal);
        CAST_BLOCK.put(metal, registry.register(new ItemBlock(castBlock, new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.castBlock(metal)));
      }
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Map<Metal, Item> castItems = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        if(cast.isValidForMetal(metal) && cast.itemForMetal(metal) == null) {
          castItems.put(metal, registry.register(new CastItem(cast, metal), GradientIds.castItem(cast, metal)));
        }
      }

      CAST_ITEM.put(cast, castItems);
    }

    registry.register(new Item(new Item.Properties().group(ItemGroup.MATERIALS)), GradientIds.INFINICOAL);
    registry.register(new DebugItem(), GradientIds.DEBUG);

    registry.register(new ItemClayBucket(), GradientIds.CLAY_BUCKET);

    BlockDispenser.registerDispenseBehavior(pebble, new BehaviorProjectileDispense() {
      @Override
      protected IProjectile getProjectileEntity(final World world, final IPosition position, final ItemStack stack) {
        return new EntityPebble(world, position.getX(), position.getY(), position.getZ());
      }
    });
  }

  //TODO
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void initOreDict(final RegistryEvent.Register<Item> event) {
    GradientMod.logger.info("Registering ore dict entries");

    // Metals/metal tools
/*
    OreDictionary.registerOre("nuggetCoal", NUGGET_COAL);

    for(final Ore.Metal ore : Ores.metals()) {
      final String caps = StringUtils.capitalize(ore.name);

      OreDictionary.registerOre("ore", ORE.get(ore));
      OreDictionary.registerOre("ore" + caps, ORE.get(ore));
      OreDictionary.registerOre("crushed" + caps, crushed(ore));
      OreDictionary.registerOre("purified" + caps, purified(ore));
    }

    for(final Metal metal : Metals.all()) {
      final String caps = StringUtils.capitalize(metal.name);

      if(metal.canMakeIngots) {
        OreDictionary.registerOre("nugget", nugget(metal));
        OreDictionary.registerOre("nugget" + caps, nugget(metal));
        OreDictionary.registerOre("ingot" + caps, castItem(GradientCasts.INGOT, metal, 1));

        if(metal.elements.size() > 1) {
          boolean make = true;

          for(final Metal.MetalElement element : metal.elements) {
            if(Metals.get(element.element) == Metals.INVALID_METAL) {
              make = false;
              break;
            }
          }

          if(make) {
            OreDictionary.registerOre("alloyNugget" + caps, alloyNugget(metal));
          }
        }

        if(metal.canMakePlates) {
          OreDictionary.registerOre("plate" + caps, plate(metal));
        }
      }

      OreDictionary.registerOre("dust" + caps, dust(metal));
      OreDictionary.registerOre("block" + caps, castItem(GradientCasts.BLOCK, metal, 1));

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

    OreDictionary.registerOre("axleWood", GradientBlocks.WOODEN_AXLE);
    OreDictionary.registerOre("gearWood", WOODEN_GEAR);
*/
  }
}
