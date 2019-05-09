package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientIds;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GradientMod.MODID)
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

  @Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      GradientMod.logger.info("Registering tile entities");

      final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

      //TODO: datafixer (see https://github.com/progwml6/ironchest/blob/7ecd01921cd0466b6075b78fedb81bc9d70d8207/src/main/java/com/progwml6/ironchest/common/tileentity/IronChestEntityType.java#L91)

      registry.register(TileEntityType.Builder.create(TileFirePit::new).build(null).setRegistryName(GradientIds.FIRE_PIT));
      registry.register(TileEntityType.Builder.create(TileBellows::new).build(null).setRegistryName(GradientIds.BELLOWS));
      registry.register(TileEntityType.Builder.create(TileManualGrinder::new).build(null).setRegistryName(GradientIds.GRINDSTONE));
      registry.register(TileEntityType.Builder.create(TileMixingBasin::new).build(null).setRegistryName(GradientIds.MIXING_BASIN));
      registry.register(TileEntityType.Builder.create(TileDryingRack::new).build(null).setRegistryName(GradientIds.DRYING_RACK));
      registry.register(TileEntityType.Builder.create(TileClayCrucible::new).build(null).setRegistryName(GradientIds.CLAY_CRUCIBLE_HARDENED));
      registry.register(TileEntityType.Builder.create(TileClayOven::new).build(null).setRegistryName(GradientIds.CLAY_OVEN_HARDENED));
      registry.register(TileEntityType.Builder.create(TileHandCrank::new).build(null).setRegistryName(GradientIds.HAND_CRANK));
      registry.register(TileEntityType.Builder.create(TileFlywheel::new).build(null).setRegistryName(GradientIds.FLYWHEEL));
      registry.register(TileEntityType.Builder.create(TileWoodenAxle::new).build(null).setRegistryName(GradientIds.WOODEN_AXLE));
      registry.register(TileEntityType.Builder.create(TileWoodenGearbox::new).build(null).setRegistryName(GradientIds.WOODEN_GEARBOX));
      registry.register(TileEntityType.Builder.create(TileBronzeFurnace::new).build(null).setRegistryName(GradientIds.BRONZE_FURNACE));
      registry.register(TileEntityType.Builder.create(TileBronzeBoiler::new).build(null).setRegistryName(GradientIds.BRONZE_BOILER));
      registry.register(TileEntityType.Builder.create(TileBronzeOven::new).build(null).setRegistryName(GradientIds.BRONZE_OVEN));
      registry.register(TileEntityType.Builder.create(TileBronzeGrinder::new).build(null).setRegistryName(GradientIds.BRONZE_GRINDER));
    }
  }
}
