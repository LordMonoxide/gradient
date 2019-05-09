package lordmonoxide.gradient;

import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import net.minecraft.init.Blocks;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GradientIds {
  private GradientIds() { }

  public static final String SALT_BLOCK = "salt_block";
  public static final String PEBBLE     = "pebble";
  private static final Map<Metal, String> METAL_PEBBLES = new LinkedHashMap<>();

  public static final String FIBRE = "fibre";
  public static final String TWINE = "twine";

  public static final String BARK_OAK      = "bark_oak";
  public static final String BARK_SPRUCE   = "bark_spruce";
  public static final String BARK_BIRCH    = "bark_birch";
  public static final String BARK_JUNGLE   = "bark_jungle";
  public static final String BARK_ACACIA   = "bark_acacia";
  public static final String BARK_DARK_OAK = "bark_dark_oak";

  public static final String MULCH = "mulch";

  public static final String HIDE_COW        = "hide_cow";
  public static final String HIDE_DONKEY     = "hide_donkey";
  public static final String HIDE_HORSE      = "hide_horse";
  public static final String HIDE_LLAMA      = "hide_llama";
  public static final String HIDE_MULE       = "hide_mule";
  public static final String HIDE_OCELOT     = "hide_ocelot";
  public static final String HIDE_PIG        = "hide_pig";
  public static final String HIDE_POLAR_BEAR = "hide_polar_bear";
  public static final String HIDE_SHEEP      = "hide_sheep";
  public static final String HIDE_WOLF       = "hide_wolf";

  public static final String HIDE_RAW       = "hide_raw";
  public static final String HIDE_SALTED    = "hide_salted";
  public static final String HIDE_PRESERVED = "hide_preserved";
  public static final String HIDE_TANNED    = "hide_tanned";

  public static final String LEATHER_CORD = "leather_cord";

  public static final String HIDE_BEDDING = "hide_bedding";
  public static final String WATERSKIN    = "waterskin";

  public static final String TORCH_STAND       = "torch_stand";
  public static final String FIBRE_TORCH_UNLIT = "fibre_torch_unlit";
  public static final String FIBRE_TORCH_LIT   = "fibre_torch_lit";
  public static final String FIRE_PIT          = "fire_pit";
  public static final String BELLOWS           = "bellows";
  public static final String FIRE_STARTER      = "fire_starter";
  public static final String IGNITION_POWDER   = "ignition_powder";

  public static final String GRINDSTONE = "grindstone";
  public static final String MIXING_BASIN   = "mixing_basin";
  public static final String DRYING_RACK    = "drying_rack";

  public static final String CLAY_FURNACE_UNHARDENED  = "clay_furnace_unhardened";
  public static final String CLAY_FURNACE_HARDENED    = "clay_furnace_hardened";
  public static final String CLAY_CRUCIBLE_UNHARDENED = "clay_crucible_unhardened";
  public static final String CLAY_CRUCIBLE_HARDENED   = "clay_crucible_hardened";
  public static final String CLAY_OVEN_UNHARDENED     = "clay_oven_unhardened";
  public static final String CLAY_OVEN_HARDENED       = "clay_oven_hardened";
  private static final Map<GradientCasts.Cast, String> CLAY_CAST_UNHARDENED = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, String> CLAY_CAST_HARDENED = new LinkedHashMap<>();
  public static final String CLAY_BUCKET_UNHARDENED = "clay_bucket_unhardened";
  public static final String CLAY_BUCKET_HARDENED   = "clay_bucket_hardened";
  public static final String CLAY_BUCKET            = "clay_bucket";

  public static final String HARDENED_LOG    = "hardened_log";
  public static final String HARDENED_PLANKS = "hardened_planks";
  public static final String HARDENED_STICK  = "hardened_stick";

  public static final String WOODEN_GEAR    = "wooden_gear";
  public static final String WOODEN_AXLE    = "wooden_axle";
  public static final String WOODEN_GEARBOX = "wooden_gearbox";
  public static final String HAND_CRANK     = "hand_crank";
  public static final String FLYWHEEL       = "flywheel";

  public static final String GRINDING_HEAD       = "grinding_head";
  public static final String BRONZE_MACHINE_HULL = "bronze_machine_hull";
  public static final String BRONZE_FURNACE      = "bronze_furnace";
  public static final String BRONZE_BOILER       = "bronze_boiler";
  public static final String BRONZE_OVEN         = "bronze_oven";
  public static final String BRONZE_GRINDER      = "bronze_grinder";

  public static final String SUGARCANE_PASTE = "sugarcane_paste";
  public static final String SALT            = "salt";
  public static final String FLOUR           = "flour";
  public static final String DOUGH           = "dough";

  public static final String HIDE_BOOTS     = "hide_boots";
  public static final String HIDE_PANTS     = "hide_pants";
  public static final String HIDE_SHIRT     = "hide_shirt";
  public static final String HIDE_HEADCOVER = "hide_headcover";

  public static final String STONE_HAMMER  = "stone_hammer";
  public static final String STONE_HATCHET = "stone_hatchet";
  public static final String STONE_MATTOCK = "stone_mattock";
  public static final String STONE_PICKAXE = "stone_pickaxe";
  public static final String FLINT_KNIFE   = "flint_knife";
  public static final String BONE_AWL      = "bone_awl";
  private static final Map<GradientTools.Type, Map<Metal, String>> TOOL = new LinkedHashMap<>();

  private static final Map<Ore.Metal, String> ORE = new LinkedHashMap<>();
  private static final Map<Ore.Metal, String> CRUSHED = new LinkedHashMap<>();
  private static final Map<Ore.Metal, String> PURIFIED = new LinkedHashMap<>();
  private static final Map<Metal, String> DUST = new LinkedHashMap<>();
  public static final String DUST_FLINT = "dust.flint";
  private static final Map<Metal, String> NUGGET = new LinkedHashMap<>();
  public static final String NUGGET_COAL = "nugget.coal";
  private static final Map<Metal, String> PLATE = new LinkedHashMap<>();
  private static final Map<Metal, String> ALLOY_NUGGET = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Map<Metal, String>> CAST_ITEM = new LinkedHashMap<>();
  private static final Map<Metal, String> CAST_BLOCK = new LinkedHashMap<>();

  public static final String INFINICOAL = "infinicoal";
  public static final String DEBUG      = "debug";

  static {
    CAST_BLOCK.put(Metals.GLASS, Blocks.GLASS.getRegistryName().toString());

    for(final Metal metal : Metals.all()) {
      METAL_PEBBLES.put(metal, "pebble." + metal.name);
      DUST         .put(metal, "dust." + metal.name);
      NUGGET       .put(metal, "nugget." + metal.name);
      ALLOY_NUGGET .put(metal, "alloy_nugget." + metal.name);
      PLATE        .put(metal, "plate." + metal.name);

      if(!CAST_BLOCK.containsKey(metal)) {
        CAST_BLOCK.put(metal, "cast_block." + metal.name);
      }
    }

    for(final Ore.Metal ore : Ores.metals()) {
      ORE     .put(ore, "ore." + ore.name);
      CRUSHED .put(ore, "crushed." + ore.name);
      PURIFIED.put(ore, "purified." + ore.name);
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CAST_UNHARDENED.put(cast, "clay_cast." + cast.name + ".unhardened");
      CLAY_CAST_HARDENED  .put(cast, "clay_cast." + cast.name + ".hardened");

      final Map<Metal, String> castItems = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        castItems.put(metal, "cast_item." + cast.name + '.' + metal.name);
      }

      CAST_ITEM.put(cast, castItems);
    }

    for(final GradientTools.Type type : GradientTools.types()) {
      final Map<Metal, String> tools = new LinkedHashMap<>();

      for(final Metal metal : Metals.all()) {
        tools.put(metal, "tool." + type.cast.name + '.' + metal.name);
      }

      TOOL.put(type, tools);
    }
  }

  public static String pebble(final Metal metal) {
    return METAL_PEBBLES.get(metal);
  }

  public static String clayCastUnhardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_UNHARDENED.get(cast);
  }

  public static String clayCastHardened(final GradientCasts.Cast cast) {
    return CLAY_CAST_HARDENED.get(cast);
  }

  public static String ore(final Ore.Metal metal) {
    return ORE.get(metal);
  }

  public static String crushed(final Ore.Metal metal) {
    return CRUSHED.get(metal);
  }

  public static String purified(final Ore.Metal metal) {
    return PURIFIED.get(metal);
  }

  public static String nugget(final Metal metal) {
    return NUGGET.get(metal);
  }

  public static String dust(final Metal metal) {
    return DUST.get(metal);
  }

  public static String plate(final Metal metal) {
    return PLATE.get(metal);
  }

  public static String castItem(final GradientCasts.Cast cast, final Metal metal) {
    return CAST_ITEM.get(cast).get(metal);
  }

  public static String castBlock(final Metal metal) {
    return CAST_BLOCK.get(metal);
  }

  public static String tool(final GradientTools.Type type, final Metal metal) {
    return TOOL.get(type).get(metal);
  }

  public static String alloyNugget(final Metal metal) {
    return ALLOY_NUGGET.get(metal);
  }
}
