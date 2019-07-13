package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GradientMod.MOD_ID)
public final class GradientContainers {
  private GradientContainers() { }

  public static final ContainerType<ClayCastContainer>      CLAY_CAST = null;
  public static final ContainerType<ClayCrucibleContainer>  CLAY_CRUCIBLE = null;
  public static final ContainerType<BronzeBoilerContainer>  BRONZE_BOILER = null;
  public static final ContainerType<BronzeFurnaceContainer> BRONZE_FURNACE = null;
  public static final ContainerType<BronzeGrinderContainer> BRONZE_GRINDER = null;
  public static final ContainerType<BronzeOvenContainer>    BRONZE_OVEN = null;

  @Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
      GradientMod.logger.info("Registering tile entities");

      final IForgeRegistry<ContainerType<?>> registry = event.getRegistry();

      registry.register(new ContainerType<>(ClayCastContainer::new).setRegistryName(GradientMod.resource("clay_cast")));
      registry.register(new ContainerType<>(ClayCrucibleContainer::new).setRegistryName(GradientBlocks.CLAY_CRUCIBLE_HARDENED.getRegistryName()));
      registry.register(new ContainerType<>(BronzeBoilerContainer::new).setRegistryName(GradientBlocks.BRONZE_BOILER.getRegistryName()));
      registry.register(new ContainerType<>(BronzeFurnaceContainer::new).setRegistryName(GradientBlocks.BRONZE_FURNACE.getRegistryName()));
      registry.register(new ContainerType<>(BronzeGrinderContainer::new).setRegistryName(GradientBlocks.BRONZE_GRINDER.getRegistryName()));
      registry.register(new ContainerType<>(BronzeOvenContainer::new).setRegistryName(GradientBlocks.BRONZE_OVEN.getRegistryName()));
    }
  }
}
