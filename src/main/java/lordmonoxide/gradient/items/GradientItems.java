package lordmonoxide.gradient.items;

import com.google.common.collect.ImmutableMap;
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
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientItems {
  private GradientItems() { }

  public static final List<Item> ITEMS = new ArrayList<>();

  public static final ItemArmor.ArmorMaterial MATERIAL_HIDE = EnumHelper.addArmorMaterial("hide", GradientMod.resource("hide").toString(), 3, new int[] {1, 1, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);

  public static final Infinicoal INFINICOAL = new Infinicoal();
  public static final DebugItem  DEBUG      = new DebugItem();

  public static final Fibre FIBRE = new Fibre();
  public static final Twine TWINE = new Twine();

  public static final Hide HIDE_COW        = new Hide("hide_cow");
  public static final Hide HIDE_DONKEY     = new Hide("hide_donkey");
  public static final Hide HIDE_HORSE      = new Hide("hide_horse");
  public static final Hide HIDE_LLAMA      = new Hide("hide_llama");
  public static final Hide HIDE_MULE       = new Hide("hide_mule");
  public static final Hide HIDE_OCELOT     = new Hide("hide_ocelot");
  public static final Hide HIDE_PIG        = new Hide("hide_pig");
  public static final Hide HIDE_POLAR_BEAR = new Hide("hide_polar_bear");
  public static final Hide HIDE_SHEEP      = new Hide("hide_sheep");
  public static final Hide HIDE_WOLF       = new Hide("hide_wolf");

  public static final GradientArmour HIDE_BOOTS     = new GradientArmour("hide_boots", MATERIAL_HIDE, 0, EntityEquipmentSlot.FEET);
  public static final GradientArmour HIDE_PANTS     = new GradientArmour("hide_pants", MATERIAL_HIDE, 0, EntityEquipmentSlot.LEGS);
  public static final GradientArmour HIDE_SHIRT     = new GradientArmour("hide_shirt", MATERIAL_HIDE, 0, EntityEquipmentSlot.CHEST);
  public static final GradientArmour HIDE_HEADCOVER = new GradientArmour("hide_headcover", MATERIAL_HIDE, 0, EntityEquipmentSlot.HEAD);

  public static final HideBedding HIDE_BEDDING = new HideBedding();
  public static final Waterskin   WATERSKIN    = new Waterskin();

  public static final FireStarter  FIRE_STARTER  = new FireStarter();
  public static final StoneHammer  STONE_HAMMER  = new StoneHammer();
  public static final StoneHatchet STONE_HATCHET = new StoneHatchet();
  public static final StoneMattock STONE_MATTOCK = new StoneMattock();
  public static final FlintKnife   FLINT_KNIFE   = new FlintKnife();
  public static final BoneAwl      BONE_AWL      = new BoneAwl();

  public static final NuggetCoal NUGGET_COAL = new NuggetCoal();
  public static final DustFlint  DUST_FLINT  = new DustFlint();

  public static final GradientItem HARDENED_STICK = new GradientItem("hardened_stick", CreativeTabs.MATERIALS);

  public static final ItemClayBucket CLAY_BUCKET = new ItemClayBucket();

  public static final SugarcanePaste SUGARCANE_PASTE = new SugarcanePaste();
  public static final Salt SALT = new Salt();
  public static final Flour FLOUR = new Flour();
  public static final Dough DOUGH = new Dough();

  public static final ImmutableMap<GradientMetals.Metal, Nugget> NUGGET;
  public static final ImmutableMap<GradientMetals.Metal, Crushed> CRUSHED;
  public static final ImmutableMap<GradientMetals.Metal, CrushedPurified> CRUSHED_PURIFIED;
  public static final ImmutableMap<GradientMetals.Metal, Dust> DUST;
  public static final ImmutableMap<GradientMetals.Metal, Plate> PLATE;
  public static final ImmutableMap<GradientCasts.Cast, ImmutableMap<GradientMetals.Metal, CastItem>> CAST_ITEM;
  public static final ImmutableMap<GradientTools.Type, ImmutableMap<GradientMetals.Metal, Tool>> TOOL;
  public static final ImmutableMap<GradientMetals.Alloy, AlloyNugget> ALLOY_NUGGET;

  static {
    final ImmutableMap.Builder<GradientMetals.Metal, Nugget> nugget = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientMetals.Metal, Crushed> crushed = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientMetals.Metal, CrushedPurified> crushedPurified = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientMetals.Metal, Dust> dust = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientMetals.Metal, Plate> plate = ImmutableMap.builder();

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(metal.canMakeNuggets) {
        nugget.put(metal, new Nugget(metal));
      }

      if(metal.canMakeIngots) {
        crushed.put(metal, new Crushed(metal));
        crushedPurified.put(metal, new CrushedPurified(metal));
      }

      dust.put(metal, new Dust(metal));

      if(metal.canMakePlates) {
        plate.put(metal, new Plate(metal));
      }
    }

    NUGGET = nugget.build();
    CRUSHED = crushed.build();
    CRUSHED_PURIFIED = crushedPurified.build();
    DUST = dust.build();
    PLATE = plate.build();

    final ImmutableMap.Builder<GradientCasts.Cast, ImmutableMap<GradientMetals.Metal, CastItem>> castItems = ImmutableMap.builder();

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      final ImmutableMap.Builder<GradientMetals.Metal, CastItem> castItem = ImmutableMap.builder();

      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(cast.isValidForMetal(metal) && cast.itemForMetal(metal) == null) {
          castItem.put(metal, new CastItem(cast, metal));
        }
      }

      final ImmutableMap<GradientMetals.Metal, CastItem> built = castItem.build();

      if(!built.isEmpty()) {
        castItems.put(cast, built);
      }
    }

    CAST_ITEM = castItems.build();

    final ImmutableMap.Builder<GradientTools.Type, ImmutableMap<GradientMetals.Metal, Tool>> tools = ImmutableMap.builder();

    for(final GradientTools.Type type : GradientTools.types()) {
      final ImmutableMap.Builder<GradientMetals.Metal, Tool> tool = ImmutableMap.builder();

      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          tool.put(metal, new Tool(type, metal));
        }
      }

      final ImmutableMap<GradientMetals.Metal, Tool> built = tool.build();

      if(!built.isEmpty()) {
        tools.put(type, built);
      }
    }

    TOOL = tools.build();

    final ImmutableMap.Builder<GradientMetals.Alloy, AlloyNugget> alloyNugget = new ImmutableMap.Builder<>();

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      alloyNugget.put(alloy, new AlloyNugget(alloy.output.metal));
    }

    ALLOY_NUGGET = alloyNugget.build();
  }

  public static final GradientItem IGNITER = new Igniter();

  public static final GrindingHead GRINDING_HEAD = new GrindingHead();

  @SubscribeEvent
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientMod.logger.info("Registering items");

    final List<Block> registered = new ArrayList<>();
    registered.add(GradientBlocks.PEBBLE);
    registered.add(GradientBlocks.CLAY_CAST_UNHARDENED);
    registered.add(GradientBlocks.CLAY_CAST_HARDENED);

    final RegistrationHelper registry = new RegistrationHelper(event.getRegistry());

    registry.register(new ItemPebble(GradientBlocks.PEBBLE).setRegistryName(GradientBlocks.PEBBLE.getRegistryName()));
    registry.register(new ItemClayCastUnhardened(GradientBlocks.CLAY_CAST_UNHARDENED).setRegistryName(GradientBlocks.CLAY_CAST_UNHARDENED.getRegistryName()));
    registry.register(new ItemClayCast(GradientBlocks.CLAY_CAST_HARDENED).setRegistryName(GradientBlocks.CLAY_CAST_HARDENED.getRegistryName()));

    for(final Block block : ForgeRegistries.BLOCKS.getValuesCollection()) {
      if(registered.contains(block) || block instanceof BlockMetalFluid || !block.getRegistryName().getNamespace().equals(GradientMod.MODID)) {
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

    registry.register(HIDE_BOOTS);
    registry.register(HIDE_PANTS);
    registry.register(HIDE_SHIRT);
    registry.register(HIDE_HEADCOVER);

    registry.register(HIDE_BEDDING);
    registry.register(WATERSKIN);

    registry.register(FIRE_STARTER);
    registry.register(STONE_HAMMER);
    registry.register(STONE_HATCHET);
    registry.register(STONE_MATTOCK);
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
    OreDictionary.registerOre("toolAxe", STONE_HATCHET.getWildcardItemStack());
    OreDictionary.registerOre("toolAxe", STONE_MATTOCK.getWildcardItemStack());
    OreDictionary.registerOre("toolHoe", STONE_MATTOCK.getWildcardItemStack());

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
        final ItemStack stack = Tool.getTool(GradientTools.MATTOCK, metal, 1, OreDictionary.WILDCARD_VALUE);
        OreDictionary.registerOre("toolAxe", stack);
        OreDictionary.registerOre("toolHoe", stack);
      }
    }

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      final String name = StringUtils.capitalize(alloy.output.metal.name);

      OreDictionary.registerOre("alloyNugget" + name, AlloyNugget.get(alloy.output.metal));
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
