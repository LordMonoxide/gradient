package lordmonoxide.gradient.blocks;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import lordmonoxide.gradient.tileentities.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientBlocks {
  public static final Material MATERIAL_CLAY_MACHINE   = new Material(MapColor.BROWN);
  public static final Material MATERIAL_BRONZE_MACHINE = new Material(MapColor.GOLD);

  public static final BlockPebble PEBBLE = new BlockPebble();

  public static final ImmutableMap<Metal, BlockPebble> METAL_PEBBLES;

  static {
    final Map<Metal, BlockPebble> pebbles = new HashMap<>();

    for(final Metal metal : Metals.all()) {
      pebbles.put(metal, new BlockPebble(metal));
    }

    METAL_PEBBLES = ImmutableMap.copyOf(pebbles);
  }

  public static final BlockSalt SALT_BLOCK = new BlockSalt();

  public static final Block STRIPPED_OAK_WOOD      = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_oak_wood")).setTranslationKey("stripped_oak_wood");
  public static final Block STRIPPED_SPRUCE_WOOD   = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_spruce_wood")).setTranslationKey("stripped_spruce_wood");
  public static final Block STRIPPED_BIRCH_WOOD    = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_birch_wood")).setTranslationKey("stripped_birch_wood");
  public static final Block STRIPPED_JUNGLE_WOOD   = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_jungle_wood")).setTranslationKey("stripped_jungle_wood");
  public static final Block STRIPPED_ACACIA_WOOD   = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_acacia_wood")).setTranslationKey("stripped_acacia_wood");
  public static final Block STRIPPED_DARK_OAK_WOOD = new BlockLog().setRegistryName(new ResourceLocation("minecraft", "stripped_dark_oak_wood")).setTranslationKey("stripped_dark_oak_wood");

  public static final BlockFirePit    FIRE_PIT          = new BlockFirePit();
  public static final BlockBellows    BELLOWS           = new BlockBellows();
  public static final BlockTorchLit   FIBRE_TORCH_LIT   = new BlockTorchLit("fibre_torch_lit", 0.67f, 0.9375f);
  public static final BlockTorchUnlit FIBRE_TORCH_UNLIT = new BlockTorchUnlit("fibre_torch_unlit", FIBRE_TORCH_LIT);

  public static final BlockManualGrinder MANUAL_GRINDER = new BlockManualGrinder();
  public static final BlockMixingBasin   MIXING_BASIN   = new BlockMixingBasin();

  public static final BlockDryingRack DRYING_RACK = new BlockDryingRack();

  public static final BlockStandingTorch STANDING_TORCH = new BlockStandingTorch();

  public static final BlockLog            HARDENED_LOG    = new BlockLog("hardened_log");
  public static final BlockHardenedPlanks HARDENED_PLANKS = new BlockHardenedPlanks();

  public static final BlockHandCrank     HAND_CRANK     = new BlockHandCrank();
  public static final BlockFlywheel      FLYWHEEL       = new BlockFlywheel();
  public static final BlockWoodenAxle    WOODEN_AXLE    = new BlockWoodenAxle();
  public static final BlockWoodenGearbox WOODEN_GEARBOX = new BlockWoodenGearbox();

  public static final BlockClayFurnace            CLAY_FURNACE_UNHARDENED  = BlockClayFurnace.unhardened();
  public static final BlockClayFurnace            CLAY_FURNACE_HARDENED    = BlockClayFurnace.hardened();
  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = new BlockClayCrucibleUnhardened();
  public static final BlockClayCrucibleHardened   CLAY_CRUCIBLE_HARDENED   = new BlockClayCrucibleHardened();
  public static final BlockClayOvenUnhardened     CLAY_OVEN_UNHARDENED     = new BlockClayOvenUnhardened();
  public static final BlockClayOvenHardened       CLAY_OVEN_HARDENED       = new BlockClayOvenHardened();

  private static final Map<GradientCasts.Cast, BlockClayCast> CLAY_CASTS_UNHARDENED = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, BlockClayCast> CLAY_CASTS_HARDENED   = new LinkedHashMap<>();

  static {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CASTS_UNHARDENED.put(cast, BlockClayCast.unhardened(cast));
      CLAY_CASTS_HARDENED.put(cast, BlockClayCast.hardened(cast));
    }
  }

  public static final BlockClayBucket CLAY_BUCKET_UNHARDENED = BlockClayBucket.unhardened();
  public static final BlockClayBucket CLAY_BUCKET_HARDENED   = BlockClayBucket.hardened();

  public static final BlockBronzeMachineHull BRONZE_MACHINE_HULL = new BlockBronzeMachineHull();
  public static final BlockBronzeFurnace     BRONZE_FURNACE      = new BlockBronzeFurnace();
  public static final BlockBronzeBoiler      BRONZE_BOILER       = new BlockBronzeBoiler();
  public static final BlockBronzeOven        BRONZE_OVEN         = new BlockBronzeOven();
  public static final BlockBronzeGrinder     BRONZE_GRINDER      = new BlockBronzeGrinder();

  private static final Map<Ore.Metal, Block> ORES = new LinkedHashMap<>();

  static {
    for(final Ore.Metal ore : Ores.metals()) {
      ORES.put(ore, new BlockOre(ore));
    }
  }

  private static final Map<Metal, Block> CAST_BLOCK = new LinkedHashMap<>();

  static {
    CAST_BLOCK.put(Metals.GLASS, Blocks.GLASS);

    for(final Metal metal : Metals.all()) {
      if(!CAST_BLOCK.containsKey(metal)) {
        CAST_BLOCK.put(metal, new CastBlock(metal));
      }
    }
  }

  private GradientBlocks() { }

  public static Block clayCastUnhardened(final GradientCasts.Cast cast) {
    return CLAY_CASTS_UNHARDENED.get(cast);
  }

  public static Block clayCastHardened(final GradientCasts.Cast cast) {
    return CLAY_CASTS_HARDENED.get(cast);
  }

  public static Block ore(final Ore.Metal ore) {
    return ORES.get(ore);
  }

  public static Block castBlock(final Metal metal) {
    return CAST_BLOCK.get(metal);
  }

  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    GradientMod.logger.info("Registering blocks");

    final IForgeRegistry<Block> registry = event.getRegistry();

    ORES.values().forEach(registry::register);

    registry.register(PEBBLE);
    METAL_PEBBLES.values().forEach(registry::register);

    registry.register(SALT_BLOCK);

    registry.register(STRIPPED_OAK_WOOD);
    registry.register(STRIPPED_SPRUCE_WOOD);
    registry.register(STRIPPED_BIRCH_WOOD);
    registry.register(STRIPPED_JUNGLE_WOOD);
    registry.register(STRIPPED_ACACIA_WOOD);
    registry.register(STRIPPED_DARK_OAK_WOOD);

    registry.register(FIRE_PIT);
    registry.register(BELLOWS);
    registry.register(FIBRE_TORCH_LIT);
    registry.register(FIBRE_TORCH_UNLIT);

    registry.register(MANUAL_GRINDER);
    registry.register(MIXING_BASIN);

    registry.register(DRYING_RACK);

    registry.register(STANDING_TORCH);

    registry.register(HARDENED_LOG);
    registry.register(HARDENED_PLANKS);

    registry.register(HAND_CRANK);
    registry.register(FLYWHEEL);
    registry.register(WOODEN_AXLE);
    registry.register(WOODEN_GEARBOX);

    registry.register(CLAY_FURNACE_UNHARDENED);
    registry.register(CLAY_FURNACE_HARDENED);
    registry.register(CLAY_CRUCIBLE_UNHARDENED);
    registry.register(CLAY_CRUCIBLE_HARDENED);
    registry.register(CLAY_OVEN_UNHARDENED);
    registry.register(CLAY_OVEN_HARDENED);
    CLAY_CASTS_UNHARDENED.values().forEach(registry::register);
    CLAY_CASTS_HARDENED.values().forEach(registry::register);
    registry.register(CLAY_BUCKET_UNHARDENED);
    registry.register(CLAY_BUCKET_HARDENED);

    registry.register(BRONZE_MACHINE_HULL);
    registry.register(BRONZE_FURNACE);
    registry.register(BRONZE_BOILER);
    registry.register(BRONZE_OVEN);
    registry.register(BRONZE_GRINDER);

    for(final Block castBlock : CAST_BLOCK.values()) {
      if(GradientMod.MODID.equals(castBlock.getRegistryName().getNamespace())) {
        registry.register(castBlock);
      }
    }

    GameRegistry.registerTileEntity(TileFirePit.class,       FIRE_PIT.getRegistryName());
    GameRegistry.registerTileEntity(TileBellows.class,       BELLOWS.getRegistryName());
    GameRegistry.registerTileEntity(TileManualGrinder.class, MANUAL_GRINDER.getRegistryName());
    GameRegistry.registerTileEntity(TileMixingBasin.class,   MIXING_BASIN.getRegistryName());

    GameRegistry.registerTileEntity(TileDryingRack.class, DRYING_RACK.getRegistryName());

    GameRegistry.registerTileEntity(TileHandCrank.class,     HAND_CRANK.getRegistryName());
    GameRegistry.registerTileEntity(TileFlywheel.class,      FLYWHEEL.getRegistryName());
    GameRegistry.registerTileEntity(TileWoodenAxle.class,    WOODEN_AXLE.getRegistryName());
    GameRegistry.registerTileEntity(TileWoodenGearbox.class, WOODEN_GEARBOX.getRegistryName());

    GameRegistry.registerTileEntity(TileClayCrucible.class, CLAY_CRUCIBLE_HARDENED.getRegistryName());
    GameRegistry.registerTileEntity(TileClayOven.class,     CLAY_OVEN_HARDENED.getRegistryName());

    GameRegistry.registerTileEntity(TileBronzeFurnace.class, BRONZE_FURNACE.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeBoiler.class,  BRONZE_BOILER.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeOven.class,    BRONZE_OVEN.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeGrinder.class, BRONZE_GRINDER.getRegistryName());
  }
}
