package lordmonoxide.gradient.items;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.entities.EntityPebble;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GradientItems {
  private GradientItems() { }

  public static final List<Item> ITEMS = new ArrayList<>();

  public static final Item SALT_BLOCK = new ItemPebble(GradientBlocks.SALT_BLOCK).setRegistryName(GradientBlocks.SALT_BLOCK.getRegistryName());
  public static final Item PEBBLE = new ItemPebble(GradientBlocks.PEBBLE).setRegistryName(GradientBlocks.PEBBLE.getRegistryName());

  public static final ImmutableMap<GradientMetals.Metal, Item> METAL_PEBBLES;

  static {
    final Map<GradientMetals.Metal, Item> pebbles = new HashMap<>();

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final Block pebble = GradientBlocks.METAL_PEBBLES.get(metal);
      pebbles.put(metal, new ItemPebble(pebble).setRegistryName(pebble.getRegistryName()));
    }

    METAL_PEBBLES = ImmutableMap.copyOf(pebbles);
  }

  public static final GradientItem FIBRE = new GradientItem("fibre", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem TWINE = new GradientItem("twine", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final GradientItem BARK_OAK      = new GradientItem("bark_oak", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem BARK_SPRUCE   = new GradientItem("bark_spruce", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem BARK_BIRCH    = new GradientItem("bark_birch", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem BARK_JUNGLE   = new GradientItem("bark_jungle", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem BARK_ACACIA   = new GradientItem("bark_acacia", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem BARK_DARK_OAK = new GradientItem("bark_dark_oak", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final Mulch MULCH = new Mulch();

  public static final GradientItem HIDE_COW        = new GradientItem("hide_cow", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_DONKEY     = new GradientItem("hide_donkey", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_HORSE      = new GradientItem("hide_horse", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_LLAMA      = new GradientItem("hide_llama", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_MULE       = new GradientItem("hide_mule", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_OCELOT     = new GradientItem("hide_ocelot", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_PIG        = new GradientItem("hide_pig", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_POLAR_BEAR = new GradientItem("hide_polar_bear", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_SHEEP      = new GradientItem("hide_sheep", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_WOLF       = new GradientItem("hide_wolf", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final GradientItem HIDE_RAW       = new GradientItem("hide_raw", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_SALTED    = new GradientItem("hide_salted", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_PRESERVED = new GradientItem("hide_preserved", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final GradientItem HIDE_TANNED    = new GradientItem("hide_tanned", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final GradientItem LEATHER_CORD = new GradientItem("leather_cord", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final HideBedding  HIDE_BEDDING = new HideBedding();
  public static final Waterskin    WATERSKIN    = new Waterskin();

  public static final Item         STANDING_TORCH    = new ItemBlock(GradientBlocks.STANDING_TORCH, new Item.Properties()).setRegistryName(GradientBlocks.STANDING_TORCH.getRegistryName());
  public static final Item         FIBRE_TORCH_UNLIT = new ItemBlock(GradientBlocks.FIBRE_TORCH_UNLIT, new Item.Properties()).setRegistryName(GradientBlocks.FIBRE_TORCH_UNLIT.getRegistryName());
  public static final Item         FIBRE_TORCH_LIT   = new ItemBlock(GradientBlocks.FIBRE_TORCH_LIT, new Item.Properties()).setRegistryName(GradientBlocks.FIBRE_TORCH_LIT.getRegistryName());
  public static final Item         FIRE_PIT          = new ItemBlock(GradientBlocks.FIRE_PIT, new Item.Properties()).setRegistryName(GradientBlocks.FIRE_PIT.getRegistryName());
  public static final FireStarter  FIRE_STARTER      = new FireStarter();
  public static final GradientItem IGNITER           = new GradientItem("igniter", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final Item MANUAL_GRINDER = new ItemBlock(GradientBlocks.MANUAL_GRINDER, new Item.Properties()).setRegistryName(GradientBlocks.MANUAL_GRINDER.getRegistryName());
  public static final Item MIXING_BASIN   = new ItemBlock(GradientBlocks.MIXING_BASIN, new Item.Properties()).setRegistryName(GradientBlocks.MIXING_BASIN.getRegistryName());
  public static final Item DRYING_RACK    = new ItemBlock(GradientBlocks.DRYING_RACK, new Item.Properties()).setRegistryName(GradientBlocks.DRYING_RACK.getRegistryName());

  public static final Item CLAY_FURNACE_UNHARDENED  = new ItemBlock(GradientBlocks.CLAY_FURNACE_UNHARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_FURNACE_UNHARDENED.getRegistryName());
  public static final Item CLAY_FURNACE_HARDENED    = new ItemBlock(GradientBlocks.CLAY_FURNACE_HARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_FURNACE_HARDENED.getRegistryName());
  public static final Item CLAY_CRUCIBLE_UNHARDENED = new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_UNHARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_UNHARDENED.getRegistryName());
  public static final Item CLAY_CRUCIBLE_HARDENED   = new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_HARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getRegistryName());
  public static final Item CLAY_OVEN_UNHARDENED     = new ItemBlock(GradientBlocks.CLAY_OVEN_UNHARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_OVEN_UNHARDENED.getRegistryName());
  public static final Item CLAY_OVEN_HARDENED       = new ItemBlock(GradientBlocks.CLAY_OVEN_HARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_OVEN_HARDENED.getRegistryName());

  public static final ImmutableMap<GradientCasts.Cast, Item> CLAY_CAST_UNHARDENED;
  public static final ImmutableMap<GradientCasts.Cast, Item> CLAY_CAST_HARDENED;

  static {
    final ImmutableMap.Builder<GradientCasts.Cast, Item> unhardened = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientCasts.Cast, Item> hardened = ImmutableMap.builder();

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Block blockUnhardened = GradientBlocks.CLAY_CASTS_UNHARDENED.get(cast);
      final Block blockHardened = GradientBlocks.CLAY_CASTS_HARDENED.get(cast);

      unhardened.put(cast, new ItemClayCastUnhardened(blockUnhardened, cast, new Item.Properties()).setRegistryName(blockUnhardened.getRegistryName()));
      hardened.put(cast, new ItemBlock(blockHardened, new Item.Properties()).setRegistryName(blockHardened.getRegistryName()));
    }

    CLAY_CAST_UNHARDENED = unhardened.build();
    CLAY_CAST_HARDENED = hardened.build();
  }

  public static final Item CLAY_BUCKET_UNHARDENED   = new ItemBlock(GradientBlocks.CLAY_BUCKET_UNHARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_BUCKET_UNHARDENED.getRegistryName());
  public static final Item CLAY_BUCKET_HARDENED     = new ItemBlock(GradientBlocks.CLAY_BUCKET_HARDENED, new Item.Properties()).setRegistryName(GradientBlocks.CLAY_BUCKET_HARDENED.getRegistryName());

  public static final Item HARDENED_LOG    = new ItemBlock(GradientBlocks.HARDENED_LOG, new Item.Properties()).setRegistryName(GradientBlocks.HARDENED_LOG.getRegistryName());
  public static final Item HARDENED_PLANKS = new ItemBlock(GradientBlocks.HARDENED_PLANKS, new Item.Properties()).setRegistryName(GradientBlocks.HARDENED_PLANKS.getRegistryName());
  public static final Item HARDENED_STICK  = new GradientItem("hardened_stick", new Item.Properties().group(ItemGroup.MATERIALS));

  public static final Item WOODEN_GEAR = new GradientItem("wooden_gear", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final Item WOODEN_AXLE = new ItemBlock(GradientBlocks.WOODEN_AXLE, new Item.Properties()).setRegistryName(GradientBlocks.WOODEN_AXLE.getRegistryName());
  public static final Item WOODEN_GEARBOX = new ItemBlock(GradientBlocks.WOODEN_GEARBOX, new Item.Properties()).setRegistryName(GradientBlocks.WOODEN_GEARBOX.getRegistryName());
  public static final Item HAND_CRANK = new ItemBlock(GradientBlocks.HAND_CRANK, new Item.Properties()).setRegistryName(GradientBlocks.HAND_CRANK.getRegistryName());
  public static final Item FLYWHEEL = new ItemFlywheel(GradientBlocks.FLYWHEEL).setRegistryName(GradientBlocks.FLYWHEEL.getRegistryName());

  public static final Item GRINDING_HEAD       = new GradientItem("grinding_head", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final Item BRONZE_MACHINE_HULL = new ItemBlock(GradientBlocks.BRONZE_MACHINE_HULL, new Item.Properties()).setRegistryName(GradientBlocks.BRONZE_MACHINE_HULL.getRegistryName());
  public static final Item BRONZE_FURNACE      = new ItemBlock(GradientBlocks.BRONZE_FURNACE, new Item.Properties()).setRegistryName(GradientBlocks.BRONZE_FURNACE.getRegistryName());
  public static final Item BRONZE_BOILER       = new ItemBlock(GradientBlocks.BRONZE_BOILER, new Item.Properties()).setRegistryName(GradientBlocks.BRONZE_BOILER.getRegistryName());
  public static final Item BRONZE_OVEN         = new ItemBlock(GradientBlocks.BRONZE_OVEN, new Item.Properties()).setRegistryName(GradientBlocks.BRONZE_OVEN.getRegistryName());
  public static final Item BRONZE_GRINDER      = new ItemBlock(GradientBlocks.BRONZE_GRINDER, new Item.Properties()).setRegistryName(GradientBlocks.BRONZE_GRINDER.getRegistryName());

  public static final GradientItem SUGARCANE_PASTE = new GradientItem("sugarcane_paste", new Item.Properties().group(ItemGroup.FOOD));
  public static final GradientItem SALT = new GradientItem("salt", new Item.Properties().group(ItemGroup.FOOD));
  public static final GradientItem FLOUR = new GradientItem("flour", new Item.Properties().group(ItemGroup.FOOD));
  public static final GradientItem DOUGH = new GradientItem("dough", new Item.Properties().group(ItemGroup.FOOD));

  public static final GradientArmour HIDE_BOOTS     = new GradientArmour("hide_boots", GradientArmourMaterial.HIDE, EntityEquipmentSlot.FEET, new Item.Properties());
  public static final GradientArmour HIDE_PANTS     = new GradientArmour("hide_pants", GradientArmourMaterial.HIDE, EntityEquipmentSlot.LEGS, new Item.Properties());
  public static final GradientArmour HIDE_SHIRT     = new GradientArmour("hide_shirt", GradientArmourMaterial.HIDE, EntityEquipmentSlot.CHEST, new Item.Properties());
  public static final GradientArmour HIDE_HEADCOVER = new GradientArmour("hide_headcover", GradientArmourMaterial.HIDE, EntityEquipmentSlot.HEAD, new Item.Properties());

  public static final StoneHammer  STONE_HAMMER  = new StoneHammer();
  public static final StoneHatchet STONE_HATCHET = new StoneHatchet();
  public static final StoneMattock STONE_MATTOCK = new StoneMattock();
  public static final StonePickaxe STONE_PICKAXE = new StonePickaxe();
  public static final FlintKnife   FLINT_KNIFE   = new FlintKnife();
  public static final BoneAwl      BONE_AWL      = new BoneAwl();
  private static final Map<GradientTools.Type, Map<GradientMetals.Metal, Tool>> TOOL = new HashMap<>();

  private static final Map<GradientMetals.Metal, Item> ORE = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> NUGGET = new HashMap<>();
  public static final GradientItem NUGGET_COAL = new GradientItem("nugget.coal", new Item.Properties().group(ItemGroup.MATERIALS));
  private static final Map<GradientMetals.Metal, ItemMetal> CRUSHED = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> PURIFIED = new HashMap<>();
  private static final Map<GradientMetals.Metal, ItemMetal> DUST = new HashMap<>();
  public static final GradientItem DUST_FLINT  = new GradientItem("dust.flint", new Item.Properties().group(ItemGroup.MATERIALS));
  private static final Map<GradientMetals.Metal, ItemMetal> PLATE = new HashMap<>();
  private static final Map<GradientMetals.Alloy, ItemMetal> ALLOY_NUGGET = new HashMap<>();
  private static final Map<GradientCasts.Cast, Map<GradientMetals.Metal, CastItem>> CAST_ITEM = new HashMap<>();
  private static final Map<GradientMetals.Metal, Item> CAST_BLOCK = new HashMap<>();

  static {
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final Block ore = GradientBlocks.ORES.get(metal);
      ORE.put(metal, new ItemBlock(ore, new Item.Properties()).setRegistryName(ore.getRegistryName()));

      final Block castBlock = GradientBlocks.CAST_BLOCK.get(metal);
      CAST_BLOCK.put(metal, new ItemBlock(castBlock, new Item.Properties()).setRegistryName(castBlock.getRegistryName()));

      if(metal.canMakeNuggets) {
        NUGGET.put(metal, new ItemMetal("nugget", metal));
      }

      if(metal.canMakeIngots) {
        CRUSHED.put(metal, new ItemMetal("crushed", metal));
        PURIFIED.put(metal, new ItemMetal("purified", metal));
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

  public static final GradientItem INFINICOAL = new GradientItem("infinicoal", new Item.Properties().group(ItemGroup.MATERIALS));
  public static final DebugItem    DEBUG      = new DebugItem();

  public static final ItemClayBucket CLAY_BUCKET = new ItemClayBucket();

  public static ItemMetal nugget(final GradientMetals.Metal metal) {
    return NUGGET.get(metal);
  }

  public static ItemMetal crushed(final GradientMetals.Metal metal) {
    return CRUSHED.get(metal);
  }

  public static ItemMetal purified(final GradientMetals.Metal metal) {
    return PURIFIED.get(metal);
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

    registry.register(SALT_BLOCK);
    registry.register(PEBBLE);
    METAL_PEBBLES.values().forEach(registry::register);

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

    registry.register(LEATHER_CORD);

    registry.register(HIDE_BEDDING);
    registry.register(WATERSKIN);

    registry.register(STANDING_TORCH);
    registry.register(FIBRE_TORCH_UNLIT);
    registry.register(FIBRE_TORCH_LIT);
    registry.register(FIRE_PIT);
    registry.register(FIRE_STARTER);
    registry.register(IGNITER);

    registry.register(MANUAL_GRINDER);
    registry.register(MIXING_BASIN);
    registry.register(DRYING_RACK);

    registry.register(CLAY_FURNACE_UNHARDENED);
    registry.register(CLAY_FURNACE_HARDENED);
    registry.register(CLAY_CRUCIBLE_UNHARDENED);
    registry.register(CLAY_CRUCIBLE_HARDENED);
    registry.register(CLAY_OVEN_UNHARDENED);
    registry.register(CLAY_OVEN_HARDENED);
    CLAY_CAST_UNHARDENED.values().forEach(registry::register);
    CLAY_CAST_HARDENED.values().forEach(registry::register);
    registry.register(CLAY_BUCKET_UNHARDENED);
    registry.register(CLAY_BUCKET_HARDENED);

    registry.register(HARDENED_LOG);
    registry.register(HARDENED_PLANKS);
    registry.register(HARDENED_STICK);

    registry.register(WOODEN_GEAR);
    registry.register(WOODEN_AXLE);
    registry.register(WOODEN_GEARBOX);
    registry.register(HAND_CRANK);
    registry.register(FLYWHEEL);

    registry.register(GRINDING_HEAD);
    registry.register(BRONZE_MACHINE_HULL);
    registry.register(BRONZE_FURNACE);
    registry.register(BRONZE_BOILER);
    registry.register(BRONZE_OVEN);
    registry.register(BRONZE_GRINDER);

    registry.register(SUGARCANE_PASTE);
    registry.register(SALT);
    registry.register(FLOUR);
    registry.register(DOUGH);

    registry.register(HIDE_BOOTS);
    registry.register(HIDE_PANTS);
    registry.register(HIDE_SHIRT);
    registry.register(HIDE_HEADCOVER);

    registry.register(STONE_HAMMER);
    registry.register(STONE_HATCHET);
    registry.register(STONE_MATTOCK);
    registry.register(STONE_PICKAXE);
    registry.register(FLINT_KNIFE);
    registry.register(BONE_AWL);
    TOOL.values().forEach(map -> map.values().forEach(registry::register));

    ORE.values().forEach(registry::register);
    NUGGET.values().forEach(registry::register);
    registry.register(NUGGET_COAL);
    CRUSHED.values().forEach(registry::register);
    PURIFIED.values().forEach(registry::register);
    DUST.values().forEach(registry::register);
    registry.register(DUST_FLINT);
    PLATE.values().forEach(registry::register);
    ALLOY_NUGGET.values().forEach(registry::register);
    CAST_ITEM.values().forEach(map -> map.values().forEach(registry::register));
    CAST_BLOCK.values().forEach(registry::register);

    registry.register(INFINICOAL);
    registry.register(DEBUG);

    registry.register(CLAY_BUCKET);

    BlockDispenser.registerDispenseBehavior(GradientBlocks.PEBBLE.asItem(), new BehaviorProjectileDispense() {
      @Override
      protected IProjectile getProjectileEntity(final World world, final IPosition position, final ItemStack stack) {
        return new EntityPebble(world, position.getX(), position.getY(), position.getZ());
      }
    });
  }

  //TODO
/*
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
    OreDictionary.registerOre("nuggetCoal", NUGGET_COAL);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String caps = StringUtils.capitalize(metal.name);

      OreDictionary.registerOre("ore", GradientBlocks.ORES.get(metal));
      OreDictionary.registerOre("ore" + caps, GradientBlocks.ORES.get(metal));

      if(metal.canMakeNuggets) {
        OreDictionary.registerOre("nugget", nugget(metal));
        OreDictionary.registerOre("nugget" + caps, nugget(metal));
        OreDictionary.registerOre("crushed" + caps, crushed(metal));
        OreDictionary.registerOre("purified" + caps, purified(metal));
      }

      OreDictionary.registerOre("dust" + caps, dust(metal));

      if(metal.canMakeIngots) {
        OreDictionary.registerOre("ingot" + caps, castItem(GradientCasts.INGOT, metal, 1));
      }

      OreDictionary.registerOre("block" + caps, castItem(GradientCasts.BLOCK, metal, 1));

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
*/

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
  }
}
