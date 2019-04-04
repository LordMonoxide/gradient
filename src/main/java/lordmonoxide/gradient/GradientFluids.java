package lordmonoxide.gradient;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GradientFluids {
  private GradientFluids() { }

  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    GradientMod.logger.info("Registering fluids");

    GradientMetals.metals.forEach(metal -> registerFluidForMetal(event.getRegistry(), metal));
  }

  private static void registerFluidForMetal(final IForgeRegistry<Block> registry, final GradientMetals.Metal metal) {
    //TODO
/*
    final Fluid fluid;

    if(FluidRegistry.isFluidRegistered(metal.name)) {
      fluid = FluidRegistry.getFluid(metal.name);
    } else {
      fluid = new Fluid(metal.name, GradientMod.resource("blocks/fluid_" + metal.name), GradientMod.resource("blocks/fluid_" + metal.name + "_flowing"))
        .setDensity(3000)
        .setLuminosity(9)
        .setViscosity(5000)
        .setTemperature((int)(metal.meltTemp + 273.15));

      FluidRegistry.registerFluid(fluid);
    }

    FluidRegistry.addBucketForFluid(fluid);

    final Block block = new BlockMetalFluid(fluid);

    if(fluid.getBlock() == null) {
      fluid.setBlock(block);
    }

    registry.register(block);

    metal.fluid = fluid;
*/
  }
}
