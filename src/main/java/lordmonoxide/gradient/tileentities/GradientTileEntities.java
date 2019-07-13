package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientIds;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GradientMod.MOD_ID)
public final class GradientTileEntities {
  private GradientTileEntities() { }

  public static final TileEntityType<?> FIRE_PIT = null;
  public static final TileEntityType<?> BELLOWS = null;
  public static final TileEntityType<?> GRINDSTONE = null;
  public static final TileEntityType<?> MIXING_BASIN = null;
  public static final TileEntityType<?> DRYING_RACK = null;
  public static final TileEntityType<?> CLAY_CRUCIBLE_HARDENED = null;
  public static final TileEntityType<?> CLAY_OVEN_HARDENED = null;
  public static final TileEntityType<?> HAND_CRANK = null;
  public static final TileEntityType<?> FLYWHEEL = null;
  public static final TileEntityType<?> WOODEN_AXLE = null;
  public static final TileEntityType<?> WOODEN_GEARBOX = null;
  public static final TileEntityType<?> BRONZE_FURNACE = null;
  public static final TileEntityType<?> BRONZE_BOILER = null;
  public static final TileEntityType<?> BRONZE_OVEN = null;
  public static final TileEntityType<?> BRONZE_GRINDER = null;

  @Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      GradientMod.logger.info("Registering tile entities");

      final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

      //TODO: datafixer (see https://github.com/progwml6/ironchest/blob/7ecd01921cd0466b6075b78fedb81bc9d70d8207/src/main/java/com/progwml6/ironchest/common/tileentity/IronChestEntityType.java#L91)

      registry.register(TileEntityType.Builder.create(TileFirePit::new, GradientBlocks.FIRE_PIT).build(null).setRegistryName(GradientIds.FIRE_PIT));
      registry.register(TileEntityType.Builder.create(TileBellows::new, GradientBlocks.BELLOWS).build(null).setRegistryName(GradientIds.BELLOWS));
      registry.register(TileEntityType.Builder.create(TileManualGrinder::new, GradientBlocks.GRINDSTONE).build(null).setRegistryName(GradientIds.GRINDSTONE));
      registry.register(TileEntityType.Builder.create(TileMixingBasin::new, GradientBlocks.MIXING_BASIN).build(null).setRegistryName(GradientIds.MIXING_BASIN));
      registry.register(TileEntityType.Builder.create(TileDryingRack::new, GradientBlocks.DRYING_RACK).build(null).setRegistryName(GradientIds.DRYING_RACK));
      registry.register(TileEntityType.Builder.create(TileClayCrucible::new, GradientBlocks.CLAY_CRUCIBLE_HARDENED).build(null).setRegistryName(GradientIds.CLAY_CRUCIBLE_HARDENED));
      registry.register(TileEntityType.Builder.create(TileClayOven::new, GradientBlocks.CLAY_OVEN_HARDENED).build(null).setRegistryName(GradientIds.CLAY_OVEN_HARDENED));
      registry.register(TileEntityType.Builder.create(TileHandCrank::new, GradientBlocks.HAND_CRANK).build(null).setRegistryName(GradientIds.HAND_CRANK));
      registry.register(TileEntityType.Builder.create(TileFlywheel::new, GradientBlocks.FLYWHEEL).build(null).setRegistryName(GradientIds.FLYWHEEL));
      registry.register(TileEntityType.Builder.create(TileWoodenAxle::new, GradientBlocks.WOODEN_AXLE).build(null).setRegistryName(GradientIds.WOODEN_AXLE));
      registry.register(TileEntityType.Builder.create(TileWoodenGearbox::new, GradientBlocks.WOODEN_GEARBOX).build(null).setRegistryName(GradientIds.WOODEN_GEARBOX));
      registry.register(TileEntityType.Builder.create(TileBronzeFurnace::new, GradientBlocks.BRONZE_FURNACE).build(null).setRegistryName(GradientIds.BRONZE_FURNACE));
      registry.register(TileEntityType.Builder.create(TileBronzeBoiler::new, GradientBlocks.BRONZE_BOILER).build(null).setRegistryName(GradientIds.BRONZE_BOILER));
      registry.register(TileEntityType.Builder.create(TileBronzeOven::new, GradientBlocks.BRONZE_OVEN).build(null).setRegistryName(GradientIds.BRONZE_OVEN));
      registry.register(TileEntityType.Builder.create(TileBronzeGrinder::new, GradientBlocks.BRONZE_GRINDER).build(null).setRegistryName(GradientIds.BRONZE_GRINDER));
    }
  }
}
