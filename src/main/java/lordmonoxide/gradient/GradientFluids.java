package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.BlockMetalFluid;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientFluids {
  private GradientFluids() { }

  public static final Set<Fluid> fluids = new HashSet<>();

  @SubscribeEvent
  public static void registerFluids(final RegistryEvent.Register<Block> event) {
    GradientMod.logger.info("Registering fluids");

    final IForgeRegistry<Block> registry = event.getRegistry();
    Metals.all().forEach(metal -> registerFluid(registry, metal.name, metal.meltTemp));
  }

  private static void registerFluid(final IForgeRegistry<Block> registry, final String name, final float meltTemp) {
    final Fluid fluid;

    if(FluidRegistry.isFluidRegistered(name)) {
      fluid = FluidRegistry.getFluid(name);
    } else {
      fluid = new Fluid(name, GradientMod.resource("blocks/fluid_" + name), GradientMod.resource("blocks/fluid_" + name + "_flowing"))
        .setDensity(3000)
        .setLuminosity(9)
        .setViscosity(5000)
        .setTemperature((int)(meltTemp + 273.15f));

      FluidRegistry.registerFluid(fluid);
      FluidRegistry.addBucketForFluid(fluid);
    }

    fluids.add(fluid);

    final Block block = new BlockMetalFluid(fluid);

    if(fluid.getBlock() == null) {
      fluid.setBlock(block);
    }

    registry.register(block);
  }
}
