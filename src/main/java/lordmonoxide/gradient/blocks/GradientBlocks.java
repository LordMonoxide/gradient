package lordmonoxide.gradient.blocks;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import lordmonoxide.gradient.tileentities.TileBronzeOven;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import lordmonoxide.gradient.tileentities.TileClayOven;
import lordmonoxide.gradient.tileentities.TileDryingRack;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.tileentities.TileFlywheel;
import lordmonoxide.gradient.tileentities.TileHandCrank;
import lordmonoxide.gradient.tileentities.TileManualGrinder;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import lordmonoxide.gradient.tileentities.TileWoodenAxle;
import lordmonoxide.gradient.tileentities.TileWoodenGearbox;
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
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientBlocks {
  public static final Material MATERIAL_CLAY_MACHINE   = new Material(MapColor.BROWN);
  public static final Material MATERIAL_BRONZE_MACHINE = new Material(MapColor.GOLD);

  public static final BlockPebble PEBBLE = new BlockPebble();

  public static final ImmutableMap<GradientMetals.Metal, BlockPebble> METAL_PEBBLES;

  static {
    final Map<GradientMetals.Metal, BlockPebble> pebbles = new HashMap<>();

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
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

  public static final ImmutableMap<GradientCasts.Cast, BlockClayCast> CLAY_CASTS_UNHARDENED;
  public static final ImmutableMap<GradientCasts.Cast, BlockClayCast> CLAY_CASTS_HARDENED;

  static {
    final ImmutableMap.Builder<GradientCasts.Cast, BlockClayCast> unhardened = ImmutableMap.builder();
    final ImmutableMap.Builder<GradientCasts.Cast, BlockClayCast> hardened = ImmutableMap.builder();

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      unhardened.put(cast, BlockClayCast.unhardened(cast));
      hardened.put(cast, BlockClayCast.hardened(cast));
    }

    CLAY_CASTS_UNHARDENED = unhardened.build();
    CLAY_CASTS_HARDENED = hardened.build();
  }

  public static final BlockClayBucket             CLAY_BUCKET_UNHARDENED   = BlockClayBucket.unhardened();
  public static final BlockClayBucket             CLAY_BUCKET_HARDENED     = BlockClayBucket.hardened();

  public static final BlockBronzeMachineHull BRONZE_MACHINE_HULL = new BlockBronzeMachineHull();
  public static final BlockBronzeFurnace     BRONZE_FURNACE      = new BlockBronzeFurnace();
  public static final BlockBronzeBoiler      BRONZE_BOILER       = new BlockBronzeBoiler();
  public static final BlockBronzeOven        BRONZE_OVEN         = new BlockBronzeOven();
  public static final BlockBronzeGrinder     BRONZE_GRINDER      = new BlockBronzeGrinder();

  public static final ImmutableMap<GradientMetals.Metal, Block> ORES;

  static {
    final Map<GradientMetals.Metal, Block> ores = new HashMap<>();

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      ores.put(metal, new BlockOre(metal));
    }

    ORES = ImmutableMap.copyOf(ores);
  }

  public static final ImmutableMap<GradientMetals.Metal, Block> CAST_BLOCK;

  static {
    final Map<GradientMetals.Metal, Block> castBlocks = new HashMap<>();
    castBlocks.put(GradientMetals.IRON, Blocks.IRON_BLOCK);
    castBlocks.put(GradientMetals.GOLD, Blocks.GOLD_BLOCK);
    castBlocks.put(GradientMetals.GLASS, Blocks.GLASS);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(!castBlocks.containsKey(metal)) {
        castBlocks.put(metal, new CastBlock(metal));
      }
    }

    CAST_BLOCK = ImmutableMap.copyOf(castBlocks);
  }

  private GradientBlocks() { }

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
      if(!registry.containsKey(castBlock.getRegistryName())) {
        registry.register(castBlock);
      }
    }

    GameRegistry.registerTileEntity(TileFirePit.class,       FIRE_PIT.getRegistryName());
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
