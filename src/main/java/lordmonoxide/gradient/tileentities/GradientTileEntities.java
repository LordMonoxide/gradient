package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public final class GradientTileEntities {
  private GradientTileEntities() { }

  @ObjectHolder("gradient:fire_pit")
  public static TileEntityType<?> FIRE_PIT;

  @ObjectHolder("gradient:manual_grinder")
  public static TileEntityType<?> MANUAL_GRINDER;

  @ObjectHolder("gradient:mixing_basin")
  public static TileEntityType<?> MIXING_BASIN;

  @ObjectHolder("gradient:drying_rack")
  public static TileEntityType<?> DRYING_RACK;

  @ObjectHolder("gradient:clay_crucible")
  public static TileEntityType<?> CLAY_CRUCIBLE;

  @ObjectHolder("gradient:clay_oven")
  public static TileEntityType<?> CLAY_OVEN;

  @ObjectHolder("gradient:hand_crank")
  public static TileEntityType<?> HAND_CRANK;

  @ObjectHolder("gradient:flywheel")
  public static TileEntityType<?> FLYWHEEL;

  @ObjectHolder("gradient:wooden_axle")
  public static TileEntityType<?> WOODEN_AXLE;

  @ObjectHolder("gradient:wooden_gearbox")
  public static TileEntityType<?> WOODEN_GEARBOX;

  @ObjectHolder("gradient:bronze_furnace")
  public static TileEntityType<?> BRONZE_FURNACE;

  @ObjectHolder("gradient:bronze_boiler")
  public static TileEntityType<?> BRONZE_BOILER;

  @ObjectHolder("gradient:bronze_oven")
  public static TileEntityType<?> BRONZE_OVEN;

  @ObjectHolder("gradient:bronze_grinder")
  public static TileEntityType<?> BRONZE_GRINDER;

  @Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
      GradientMod.logger.info("Registering tile entities");

      final IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();

      //TODO: datafixer (see https://github.com/progwml6/ironchest/blob/7ecd01921cd0466b6075b78fedb81bc9d70d8207/src/main/java/com/progwml6/ironchest/common/tileentity/IronChestEntityType.java#L91)

      registry.register(TileEntityType.Builder.create(TileFirePit::new).build(null).setRegistryName(GradientBlocks.FIRE_PIT.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileManualGrinder::new).build(null).setRegistryName(GradientBlocks.MANUAL_GRINDER.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileMixingBasin::new).build(null).setRegistryName(GradientBlocks.MIXING_BASIN.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileDryingRack::new).build(null).setRegistryName(GradientBlocks.DRYING_RACK.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileClayCrucible::new).build(null).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileClayOven::new).build(null).setRegistryName(GradientBlocks.CLAY_OVEN_HARDENED.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileHandCrank::new).build(null).setRegistryName(GradientBlocks.HAND_CRANK.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileFlywheel::new).build(null).setRegistryName(GradientBlocks.FLYWHEEL.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileWoodenAxle::new).build(null).setRegistryName(GradientBlocks.WOODEN_AXLE.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileWoodenGearbox::new).build(null).setRegistryName(GradientBlocks.WOODEN_GEARBOX.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileBronzeFurnace::new).build(null).setRegistryName(GradientBlocks.BRONZE_FURNACE.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileBronzeBoiler::new).build(null).setRegistryName(GradientBlocks.BRONZE_BOILER.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileBronzeOven::new).build(null).setRegistryName(GradientBlocks.BRONZE_OVEN.getRegistryName()));
      registry.register(TileEntityType.Builder.create(TileBronzeGrinder::new).build(null).setRegistryName(GradientBlocks.BRONZE_GRINDER.getRegistryName()));
    }
  }
}
