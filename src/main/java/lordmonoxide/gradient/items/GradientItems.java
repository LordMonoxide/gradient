package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.BlockOre;
import lordmonoxide.gradient.blocks.CastBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.entities.EntityPebble;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientItems {
  private GradientItems() { }

  public static final List<Item> ITEMS = new ArrayList<>();

  public static final ItemArmor.ArmorMaterial MATERIAL_HIDE = EnumHelper.addArmorMaterial("hide", GradientMod.resource("hide").toString(), 3, new int[] {1, 1, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);

  public static final Item SALT_BLOCK = new ItemPebble(GradientBlocks.SALT_BLOCK).setRegistryName(GradientBlocks.SALT_BLOCK.getRegistryName());
  public static final Item PEBBLE = new ItemPebble(GradientBlocks.PEBBLE).setRegistryName(GradientBlocks.PEBBLE.getRegistryName());

  private static final Map<Metal, Item> METAL_PEBBLES = new LinkedHashMap<>();

  static {
    for(final Metal metal : Metals.all()) {
      final Block pebble = GradientBlocks.METAL_PEBBLES.get(metal);
      METAL_PEBBLES.put(metal, new ItemPebble(pebble).setRegistryName(pebble.getRegistryName()));
    }
  }

  public static final GradientItem FIBRE = new GradientItem("fibre", CreativeTabs.MATERIALS);
  public static final GradientItem TWINE = new GradientItem("twine", CreativeTabs.MATERIALS);

  public static final Item STRIPPED_OAK_WOOD      = new ItemBlock(GradientBlocks.STRIPPED_OAK_WOOD).setRegistryName(GradientBlocks.STRIPPED_OAK_WOOD.getRegistryName());
  public static final Item STRIPPED_SPRUCE_WOOD   = new ItemBlock(GradientBlocks.STRIPPED_SPRUCE_WOOD).setRegistryName(GradientBlocks.STRIPPED_SPRUCE_WOOD.getRegistryName());
  public static final Item STRIPPED_BIRCH_WOOD    = new ItemBlock(GradientBlocks.STRIPPED_BIRCH_WOOD).setRegistryName(GradientBlocks.STRIPPED_BIRCH_WOOD.getRegistryName());
  public static final Item STRIPPED_JUNGLE_WOOD   = new ItemBlock(GradientBlocks.STRIPPED_JUNGLE_WOOD).setRegistryName(GradientBlocks.STRIPPED_JUNGLE_WOOD.getRegistryName());
  public static final Item STRIPPED_ACACIA_WOOD   = new ItemBlock(GradientBlocks.STRIPPED_ACACIA_WOOD).setRegistryName(GradientBlocks.STRIPPED_ACACIA_WOOD.getRegistryName());
  public static final Item STRIPPED_DARK_OAK_WOOD = new ItemBlock(GradientBlocks.STRIPPED_DARK_OAK_WOOD).setRegistryName(GradientBlocks.STRIPPED_DARK_OAK_WOOD.getRegistryName());

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

  public static final GradientItem LEATHER_CORD = new GradientItem("leather_cord", CreativeTabs.MATERIALS);

  public static final HideBedding  HIDE_BEDDING = new HideBedding();
  public static final Waterskin    WATERSKIN    = new Waterskin();

  public static final Item         STANDING_TORCH    = new ItemBlock(GradientBlocks.STANDING_TORCH).setRegistryName(GradientBlocks.STANDING_TORCH.getRegistryName());
  public static final Item         FIBRE_TORCH_UNLIT = new ItemBlock(GradientBlocks.FIBRE_TORCH_UNLIT).setRegistryName(GradientBlocks.FIBRE_TORCH_UNLIT.getRegistryName());
  public static final Item         FIBRE_TORCH_LIT   = new ItemBlock(GradientBlocks.FIBRE_TORCH_LIT).setRegistryName(GradientBlocks.FIBRE_TORCH_LIT.getRegistryName());
  public static final Item         FIRE_PIT          = new ItemBlock(GradientBlocks.FIRE_PIT).setRegistryName(GradientBlocks.FIRE_PIT.getRegistryName());
  public static final Item         BELLOWS           = new ItemBlock(GradientBlocks.BELLOWS).setRegistryName(GradientBlocks.BELLOWS.getRegistryName());
  public static final FireStarter  FIRE_STARTER      = new FireStarter();
  public static final GradientItem IGNITER           = new GradientItem("igniter", CreativeTabs.MATERIALS);

  public static final Item MANUAL_GRINDER = new ItemBlock(GradientBlocks.MANUAL_GRINDER).setRegistryName(GradientBlocks.MANUAL_GRINDER.getRegistryName());
  public static final Item MIXING_BASIN   = new ItemBlock(GradientBlocks.MIXING_BASIN).setRegistryName(GradientBlocks.MIXING_BASIN.getRegistryName());
  public static final Item DRYING_RACK    = new ItemBlock(GradientBlocks.DRYING_RACK).setRegistryName(GradientBlocks.DRYING_RACK.getRegistryName());

  public static final Item CLAY_FURNACE_UNHARDENED  = new ItemBlock(GradientBlocks.CLAY_FURNACE_UNHARDENED).setRegistryName(GradientBlocks.CLAY_FURNACE_UNHARDENED.getRegistryName());
  public static final Item CLAY_FURNACE_HARDENED    = new ItemBlock(GradientBlocks.CLAY_FURNACE_HARDENED).setRegistryName(GradientBlocks.CLAY_FURNACE_HARDENED.getRegistryName());
  public static final Item CLAY_CRUCIBLE_UNHARDENED = new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_UNHARDENED).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_UNHARDENED.getRegistryName());
  public static final Item CLAY_CRUCIBLE_HARDENED   = new ItemBlock(GradientBlocks.CLAY_CRUCIBLE_HARDENED).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getRegistryName());
  public static final Item CLAY_OVEN_UNHARDENED     = new ItemBlock(GradientBlocks.CLAY_OVEN_UNHARDENED).setRegistryName(GradientBlocks.CLAY_OVEN_UNHARDENED.getRegistryName());
  public static final Item CLAY_OVEN_HARDENED       = new ItemBlock(GradientBlocks.CLAY_OVEN_HARDENED).setRegistryName(GradientBlocks.CLAY_OVEN_HARDENED.getRegistryName());
  @GameRegistry.ObjectHolder("gradient:clay_metal_mixer_unhardened")
  public static final ItemBlock CLAY_METAL_MIXER_UNHARDENED = null;
  @GameRegistry.ObjectHolder("gradient:clay_metal_mixer_hardened")
  public static final ItemBlock CLAY_METAL_MIXER_HARDENED = null;

  private static final Map<GradientCasts.Cast, Item> CLAY_CAST_UNHARDENED = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Item> CLAY_CAST_HARDENED   = new LinkedHashMap<>();

  static {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Block blockUnhardened = GradientBlocks.clayCastUnhardened(cast);
      final Block blockHardened = GradientBlocks.clayCastHardened(cast);

      CLAY_CAST_UNHARDENED.put(cast, new ItemClayCastUnhardened(blockUnhardened).setRegistryName(blockUnhardened.getRegistryName()));
      CLAY_CAST_HARDENED.put(cast, new ItemBlock(blockHardened).setRegistryName(blockHardened.getRegistryName()));
    }
  }

  public static final Item CLAY_BUCKET_UNHARDENED = new ItemBlock(GradientBlocks.CLAY_BUCKET_UNHARDENED).setRegistryName(GradientBlocks.CLAY_BUCKET_UNHARDENED.getRegistryName());
  public static final Item CLAY_BUCKET_HARDENED   = new ItemBlock(GradientBlocks.CLAY_BUCKET_HARDENED).setRegistryName(GradientBlocks.CLAY_BUCKET_HARDENED.getRegistryName());

  public static final Item HARDENED_LOG    = new ItemBlock(GradientBlocks.HARDENED_LOG).setRegistryName(GradientBlocks.HARDENED_LOG.getRegistryName());
  public static final Item HARDENED_PLANKS = new ItemBlock(GradientBlocks.HARDENED_PLANKS).setRegistryName(GradientBlocks.HARDENED_PLANKS.getRegistryName());
  public static final Item HARDENED_STICK  = new GradientItem("hardened_stick", CreativeTabs.MATERIALS);

  public static final Item WOODEN_GEAR = new GradientItem("wooden_gear", CreativeTabs.MATERIALS);
  public static final Item WOODEN_AXLE = new ItemBlock(GradientBlocks.WOODEN_AXLE).setRegistryName(GradientBlocks.WOODEN_AXLE.getRegistryName());
  public static final Item WOODEN_GEARBOX = new ItemBlock(GradientBlocks.WOODEN_GEARBOX).setRegistryName(GradientBlocks.WOODEN_GEARBOX.getRegistryName());
  public static final Item HAND_CRANK = new ItemBlock(GradientBlocks.HAND_CRANK).setRegistryName(GradientBlocks.HAND_CRANK.getRegistryName());
  public static final Item FLYWHEEL = new ItemFlywheel(GradientBlocks.FLYWHEEL).setRegistryName(GradientBlocks.FLYWHEEL.getRegistryName());

  public static final Item GRINDING_HEAD       = new GradientItem("grinding_head", CreativeTabs.MATERIALS);
  public static final Item BRONZE_MACHINE_HULL = new ItemBlock(GradientBlocks.BRONZE_MACHINE_HULL).setRegistryName(GradientBlocks.BRONZE_MACHINE_HULL.getRegistryName());
  public static final Item BRONZE_FURNACE      = new ItemBlock(GradientBlocks.BRONZE_FURNACE).setRegistryName(GradientBlocks.BRONZE_FURNACE.getRegistryName());
  public static final Item BRONZE_BOILER       = new ItemBlock(GradientBlocks.BRONZE_BOILER).setRegistryName(GradientBlocks.BRONZE_BOILER.getRegistryName());
  public static final Item BRONZE_OVEN         = new ItemBlock(GradientBlocks.BRONZE_OVEN).setRegistryName(GradientBlocks.BRONZE_OVEN.getRegistryName());
  public static final Item BRONZE_GRINDER      = new ItemBlock(GradientBlocks.BRONZE_GRINDER).setRegistryName(GradientBlocks.BRONZE_GRINDER.getRegistryName());

  public static final GradientItem SUGARCANE_PASTE = new GradientItem("sugarcane_paste", CreativeTabs.FOOD);
  public static final GradientItem SALT = new GradientItem("salt", CreativeTabs.FOOD);
  public static final GradientItem FLOUR = new GradientItem("flour", CreativeTabs.FOOD);
  public static final GradientItem DOUGH = new GradientItem("dough", CreativeTabs.FOOD);

  public static final GradientArmour HIDE_BOOTS     = new GradientArmour("hide_boots", MATERIAL_HIDE, 0, EntityEquipmentSlot.FEET);
  public static final GradientArmour HIDE_PANTS     = new GradientArmour("hide_pants", MATERIAL_HIDE, 0, EntityEquipmentSlot.LEGS);
  public static final GradientArmour HIDE_SHIRT     = new GradientArmour("hide_shirt", MATERIAL_HIDE, 0, EntityEquipmentSlot.CHEST);
  public static final GradientArmour HIDE_HEADCOVER = new GradientArmour("hide_headcover", MATERIAL_HIDE, 0, EntityEquipmentSlot.HEAD);

  public static final StoneHammer  STONE_HAMMER  = new StoneHammer();
  public static final StoneHatchet STONE_HATCHET = new StoneHatchet();
  public static final StoneMattock STONE_MATTOCK = new StoneMattock();
  public static final StonePickaxe STONE_PICKAXE = new StonePickaxe();
  public static final FlintKnife   FLINT_KNIFE   = new FlintKnife();
  public static final BoneAwl      BONE_AWL      = new BoneAwl();
  private static final Map<GradientTools.Type, Map<Metal, Tool>> TOOL = new LinkedHashMap<>();

  private static final Map<Ore.Metal, Item> ORE = new LinkedHashMap<>();
  private static final Map<Ore.Metal, ItemMetal> CRUSHED = new LinkedHashMap<>();
  private static final Map<Ore.Metal, ItemMetal> PURIFIED = new LinkedHashMap<>();
  private static final Map<Metal, ItemMetal> DUST = new LinkedHashMap<>();
  public static final GradientItem DUST_FLINT  = new GradientItem("dust.flint", CreativeTabs.MATERIALS);
  private static final Map<Metal, ItemMetal> NUGGET = new LinkedHashMap<>();
  public static final GradientItem NUGGET_COAL = new GradientItem("nugget.coal", CreativeTabs.MATERIALS);
  private static final Map<Metal, ItemMetal> PLATE = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Map<Metal, CastItem>> CAST_ITEM = new LinkedHashMap<>();
  private static final Map<Metal, Item> CAST_BLOCK = new LinkedHashMap<>();

  static {
    CAST_BLOCK.put(Metals.GLASS, ItemBlock.getItemFromBlock(Blocks.GLASS));

    for(final Ore.Metal ore : Ores.metals()) {
      final BlockOre block = GradientBlocks.ore(ore);
      ORE.put(ore, new ItemOreBlock(block).setRegistryName(block.getRegistryName()));
      CRUSHED.put(ore, new ItemMetal("crushed", ore.metal));
      PURIFIED.put(ore, new ItemMetal("purified", ore.metal));
    }

    for(final Metal metal : Metals.all()) {
      DUST.put(metal, new ItemMetal("dust", metal));

      if(metal.canMakeIngots) {
        NUGGET.put(metal, new ItemMetal("nugget", metal));

        if(metal.canMakePlates) {
          PLATE.put(metal, new ItemMetal("plate", metal));
        }
      }

      if(!CAST_BLOCK.containsKey(metal)) {
        final Block castBlock = GradientBlocks.castBlock(metal);
        CAST_BLOCK.put(metal, (castBlock instanceof CastBlock ? new ItemCastBlock((CastBlock)castBlock) : new ItemBlock(castBlock)).setRegistryName(castBlock.getRegistryName()));
      }
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final Map<Metal, CastItem> castItems = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        if(cast.isValidForMetal(metal) && cast.itemForMetal(metal) == null) {
          castItems.put(metal, new CastItem(cast, metal));
        }
      }

      CAST_ITEM.put(cast, castItems);
    }

    for(final GradientTools.Type type : GradientTools.types()) {
      final Map<Metal, Tool> tools = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        if(metal.canMakeTools) {
          tools.put(metal, new Tool(type, metal));
        }
      }

      TOOL.put(type, tools);
    }
  }

  public static final GradientItem INFINICOAL = new GradientItem("infinicoal", CreativeTabs.MATERIALS);
  public static final DebugItem    DEBUG      = new DebugItem();

  public static final ItemClayBucket CLAY_BUCKET = new ItemClayBucket();

  public static Item pebble(final Metal metal) {
    return METAL_PEBBLES.get(metal);
  }

  public static Item clayCastUnhardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_UNHARDENED.get(cast);
  }

  public static Item clayCastHardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_HARDENED.get(cast);
  }

  public static ItemMetal crushed(final Ore.Metal metal) {
    return CRUSHED.get(metal);
  }

  public static ItemMetal purified(final Ore.Metal metal) {
    return PURIFIED.get(metal);
  }

  public static ItemMetal nugget(final Metal metal) {
    return NUGGET.get(metal);
  }

  public static ItemMetal dust(final Metal metal) {
    return DUST.get(metal);
  }

  public static ItemMetal plate(final Metal metal) {
    return PLATE.get(metal);
  }

  public static ItemStack castItem(final GradientCasts.Cast cast, final Metal metal, final int amount) {
    final ItemStack stack = cast.itemForMetal(metal);

    if(stack != null) {
      return ItemHandlerHelper.copyStackWithSize(stack, amount);
    }

    return CAST_ITEM.get(cast).get(metal).getItemStack(amount);
  }

  public static Tool tool(final GradientTools.Type type, final Metal metal) {
    return TOOL.get(type).get(metal);
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

    registry.register(STRIPPED_OAK_WOOD);
    registry.register(STRIPPED_SPRUCE_WOOD);
    registry.register(STRIPPED_BIRCH_WOOD);
    registry.register(STRIPPED_JUNGLE_WOOD);
    registry.register(STRIPPED_ACACIA_WOOD);
    registry.register(STRIPPED_DARK_OAK_WOOD);

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
    registry.register(BELLOWS);
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
    registry.register(new ItemBlock(GradientBlocks.CLAY_METAL_MIXER_UNHARDENED).setRegistryName(GradientBlocks.CLAY_METAL_MIXER_UNHARDENED.getRegistryName()));
    registry.register(new ItemBlock(GradientBlocks.CLAY_METAL_MIXER_HARDENED).setRegistryName(GradientBlocks.CLAY_METAL_MIXER_HARDENED.getRegistryName()));
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
    CRUSHED.values().forEach(registry::register);
    PURIFIED.values().forEach(registry::register);
    NUGGET.values().forEach(registry::register);
    registry.register(NUGGET_COAL);
    DUST.values().forEach(registry::register);
    registry.register(DUST_FLINT);
    PLATE.values().forEach(registry::register);
    CAST_ITEM.values().forEach(map -> map.values().forEach(registry::register));

    for(final Item castBlock : CAST_BLOCK.values()) {
      if(GradientMod.MODID.equals(castBlock.getRegistryName().getNamespace())) {
        registry.register(castBlock);
      }
    }

    registry.register(INFINICOAL);
    registry.register(DEBUG);

    registry.register(CLAY_BUCKET);

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

    OreDictionary.registerOre("dustFlint", DUST_FLINT);
    OreDictionary.registerOre("sand", new ItemStack(Blocks.SAND, 1, OreDictionary.WILDCARD_VALUE));

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
  }
}
