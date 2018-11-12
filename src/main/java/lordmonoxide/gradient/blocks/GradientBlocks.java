package lordmonoxide.gradient.blocks;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.bronzeboiler.BlockBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzeboiler.TileBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzefurnace.BlockBronzeFurnace;
import lordmonoxide.gradient.blocks.bronzefurnace.TileBronzeFurnace;
import lordmonoxide.gradient.blocks.bronzegrinder.BlockBronzeGrinder;
import lordmonoxide.gradient.blocks.bronzegrinder.TileBronzeGrinder;
import lordmonoxide.gradient.blocks.bronzeoven.BlockBronzeOven;
import lordmonoxide.gradient.blocks.bronzeoven.TileBronzeOven;
import lordmonoxide.gradient.blocks.claybowl.BlockClayBowl;
import lordmonoxide.gradient.blocks.claybucket.BlockClayBucket;
import lordmonoxide.gradient.blocks.claycast.BlockClayCast;
import lordmonoxide.gradient.blocks.claycast.BlockClayCastUnhardened;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucibleUnhardened;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.blocks.firepit.BlockFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import lordmonoxide.gradient.blocks.manualgrinder.BlockManualGrinder;
import lordmonoxide.gradient.blocks.manualgrinder.TileManualGrinder;
import lordmonoxide.gradient.blocks.mixingbasin.BlockMixingBasin;
import lordmonoxide.gradient.blocks.mixingbasin.TileMixingBasin;
import lordmonoxide.gradient.blocks.pebble.BlockPebble;
import lordmonoxide.gradient.blocks.torch.BlockTorchLit;
import lordmonoxide.gradient.blocks.torch.BlockTorchUnlit;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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
  public static final BlockSalt   SALT   = new BlockSalt();

  public static final BlockOreMagnesium ORE_MAGNESIUM = new BlockOreMagnesium();

  public static final BlockFirePit    FIRE_PIT          = new BlockFirePit();
  public static final BlockTorchLit   FIBRE_TORCH_LIT   = new BlockTorchLit("fibre_torch_lit", 0.67f);
  public static final BlockTorchUnlit FIBRE_TORCH_UNLIT = new BlockTorchUnlit("fibre_torch_unlit", FIBRE_TORCH_LIT);

  public static final BlockManualGrinder MANUAL_GRINDER = new BlockManualGrinder();
  public static final BlockMixingBasin   MIXING_BASIN   = new BlockMixingBasin();

  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = new BlockClayCrucibleUnhardened();
  public static final BlockClayCastUnhardened     CLAY_CAST_UNHARDENED     = new BlockClayCastUnhardened();
  public static final BlockClayBucket             CLAY_BUCKET              = new BlockClayBucket();
  public static final BlockClayFurnace            CLAY_FURNACE             = new BlockClayFurnace();
  public static final BlockClayCrucible           CLAY_CRUCIBLE            = new BlockClayCrucible();
  public static final BlockClayCast               CLAY_CAST                = new BlockClayCast();
  public static final BlockClayBowl               CLAY_BOWL                = new BlockClayBowl();

  public static final BlockBronzeMachineHull BRONZE_MACHINE_HULL = new BlockBronzeMachineHull();
  public static final BlockBronzeFurnace     BRONZE_FURNACE      = new BlockBronzeFurnace();
  public static final BlockBronzeBoiler      BRONZE_BOILER       = new BlockBronzeBoiler();
  public static final BlockBronzeOven        BRONZE_OVEN         = new BlockBronzeOven();
  public static final BlockBronzeGrinder     BRONZE_GRINDER      = new BlockBronzeGrinder();

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

    registry.register(PEBBLE);
    registry.register(SALT);

    registry.register(ORE_MAGNESIUM);

    registry.register(FIRE_PIT);
    registry.register(FIBRE_TORCH_LIT);
    registry.register(FIBRE_TORCH_UNLIT);

    registry.register(MANUAL_GRINDER);
    registry.register(MIXING_BASIN);

    registry.register(CLAY_CRUCIBLE_UNHARDENED);
    registry.register(CLAY_CAST_UNHARDENED);
    registry.register(CLAY_BUCKET);
    registry.register(CLAY_FURNACE);
    registry.register(CLAY_CRUCIBLE);
    registry.register(CLAY_CAST);
    registry.register(CLAY_BOWL);

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

    GameRegistry.registerTileEntity(TileBronzeFurnace.class, BRONZE_FURNACE.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeBoiler.class,  BRONZE_BOILER.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeOven.class,    BRONZE_OVEN.getRegistryName());
    GameRegistry.registerTileEntity(TileBronzeGrinder.class, BRONZE_GRINDER.getRegistryName());
  }
}
