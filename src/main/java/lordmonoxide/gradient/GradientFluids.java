package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.BlockMetalFluid;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientFluids {
  private GradientFluids() { }

  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    GradientMod.logger.info("Registering fluids");

    Metals.all().forEach(metal -> registerFluidForMetal(event.getRegistry(), metal));
  }

  private static void registerFluidForMetal(final IForgeRegistry<Block> registry, final Metal metal) {
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
  }
}
