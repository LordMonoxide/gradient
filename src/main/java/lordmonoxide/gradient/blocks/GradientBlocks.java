package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientIds;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import lordmonoxide.gradient.utils.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.LinkedHashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(GradientMod.MODID)
public final class GradientBlocks {
  public static final BlockSalt SALT_BLOCK = null;
  public static final BlockPebble PEBBLE = null;

  private static final Map<Metal, Block> METAL_PEBBLES = new LinkedHashMap<>();

  public static final BlockTorchStand TORCH_STAND = null;
  public static final BlockTorchUnlit FIBRE_TORCH_UNLIT = null;
  public static final BlockTorchLit FIBRE_TORCH_LIT = null;
  public static final BlockFirePit FIRE_PIT = null;
  public static final BlockBellows BELLOWS = null;

  public static final BlockGrindstone GRINDSTONE = null;
  public static final BlockMixingBasin MIXING_BASIN = null;
  public static final BlockDryingRack DRYING_RACK = null;

  public static final BlockClayFurnace CLAY_FURNACE_UNHARDENED = null;
  public static final BlockClayFurnace CLAY_FURNACE_HARDENED = null;
  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = null;
  public static final BlockClayCrucibleHardened CLAY_CRUCIBLE_HARDENED = null;
  public static final BlockClayOvenUnhardened CLAY_OVEN_UNHARDENED = null;
  public static final BlockClayOvenHardened CLAY_OVEN_HARDENED = null;
  private static final Map<GradientCasts.Cast, Block> CLAY_CASTS_UNHARDENED = new LinkedHashMap<>();
  private static final Map<GradientCasts.Cast, Block> CLAY_CASTS_HARDENED = new LinkedHashMap<>();
  public static final BlockClayBucket CLAY_BUCKET_UNHARDENED = null;
  public static final BlockClayBucket CLAY_BUCKET_HARDENED = null;

  public static final BlockLog HARDENED_LOG = null;
  public static final BlockHardenedPlanks HARDENED_PLANKS = null;

  public static final BlockWoodenAxle WOODEN_AXLE = null;
  public static final BlockWoodenGearbox WOODEN_GEARBOX = null;
  public static final BlockHandCrank HAND_CRANK = null;
  public static final BlockFlywheel FLYWHEEL = null;

  public static final BlockBronzeMachineHull BRONZE_MACHINE_HULL = null;
  public static final BlockBronzeFurnace BRONZE_FURNACE = null;
  public static final BlockBronzeBoiler BRONZE_BOILER = null;
  public static final BlockBronzeOven BRONZE_OVEN = null;
  public static final BlockBronzeGrinder BRONZE_GRINDER = null;

  private static final Map<Ore.Metal, Block> ORES = new LinkedHashMap<>();
  private static final Map<Metal, Block> CAST_BLOCK = new LinkedHashMap<>();

  private GradientBlocks() { }

  public static Block pebble(final Metal metal) {
    return METAL_PEBBLES.get(metal);
  }

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

    final RegistryHelper<Block> registry = new RegistryHelper<>(event.getRegistry());

    registry.register(new BlockSalt(), GradientIds.SALT_BLOCK);
    registry.register(new BlockPebble(), GradientIds.PEBBLE);

    for(final Metal metal : Metals.all()) {
      METAL_PEBBLES.put(metal, registry.register(new BlockPebble(metal), GradientIds.pebble(metal)));
    }

    registry.register(new BlockTorchStand(), GradientIds.TORCH_STAND);
    registry.register(new BlockTorchUnlit(() -> FIBRE_TORCH_LIT, Block.Properties.create(Material.CIRCUITS)), GradientIds.FIBRE_TORCH_UNLIT);
    registry.register(new BlockTorchLit(0.67f, 0.9375f, Block.Properties.create(Material.CIRCUITS)), GradientIds.FIBRE_TORCH_LIT);
    registry.register(new BlockFirePit(), GradientIds.FIRE_PIT);
    registry.register(new BlockBellows(), GradientIds.BELLOWS);

    registry.register(new BlockGrindstone(), GradientIds.GRINDSTONE);
    registry.register(new BlockMixingBasin(), GradientIds.MIXING_BASIN);
    registry.register(new BlockDryingRack(), GradientIds.DRYING_RACK);

    registry.register(BlockClayFurnace.unhardened(), GradientIds.CLAY_FURNACE_UNHARDENED);
    registry.register(BlockClayFurnace.hardened(), GradientIds.CLAY_FURNACE_HARDENED);
    registry.register(new BlockClayCrucibleUnhardened(), GradientIds.CLAY_CRUCIBLE_UNHARDENED);
    registry.register(new BlockClayCrucibleHardened(), GradientIds.CLAY_CRUCIBLE_HARDENED);
    registry.register(new BlockClayOvenUnhardened(), GradientIds.CLAY_OVEN_UNHARDENED);
    registry.register(new BlockClayOvenHardened(), GradientIds.CLAY_OVEN_HARDENED);

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CASTS_UNHARDENED.put(cast, registry.register(BlockClayCast.unhardened(cast), GradientIds.clayCastUnhardened(cast)));
    }

    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      CLAY_CASTS_HARDENED.put(cast, registry.register(BlockClayCast.hardened(cast), GradientIds.clayCastHardened(cast)));
    }

    registry.register(BlockClayBucket.unhardened(), GradientIds.CLAY_BUCKET_UNHARDENED);
    registry.register(BlockClayBucket.hardened(), GradientIds.CLAY_BUCKET_HARDENED);

    registry.register(new BlockLog(), GradientIds.HARDENED_LOG);
    registry.register(new BlockHardenedPlanks(), GradientIds.HARDENED_PLANKS);

    registry.register(new BlockWoodenAxle(), GradientIds.WOODEN_AXLE);
    registry.register(new BlockWoodenGearbox(), GradientIds.WOODEN_GEARBOX);
    registry.register(new BlockHandCrank(), GradientIds.HAND_CRANK);
    registry.register(new BlockFlywheel(), GradientIds.FLYWHEEL);

    registry.register(new BlockBronzeMachineHull(), GradientIds.BRONZE_MACHINE_HULL);
    registry.register(new BlockBronzeFurnace(), GradientIds.BRONZE_FURNACE);
    registry.register(new BlockBronzeBoiler(), GradientIds.BRONZE_BOILER);
    registry.register(new BlockBronzeOven(), GradientIds.BRONZE_OVEN);
    registry.register(new BlockBronzeGrinder(), GradientIds.BRONZE_GRINDER);

    for(final Ore.Metal ore : Ores.metals()) {
      ORES.put(ore, registry.register(new BlockOre(ore), GradientIds.ore(ore)));
    }

    CAST_BLOCK.put(Metals.GLASS, Blocks.GLASS);

    for(final Metal metal : Metals.all()) {
      if(!CAST_BLOCK.containsKey(metal)) {
        CAST_BLOCK.put(metal, registry.register(new CastBlock(), GradientIds.castBlock(metal)));
      }
    }
  }
}
