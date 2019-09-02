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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GradientFluids {
  private GradientFluids() { }

  public static Fluid AIR;
  public static Map<Metal, Fluid> METALS = new HashMap<>();

  public static final Set<Fluid> fluids = new HashSet<>();

  @SubscribeEvent
  public static void registerFluids(final RegistryEvent.Register<Block> event) {
    GradientMod.logger.info("Registering fluids");

    final IForgeRegistry<Block> registry = event.getRegistry();

    for(final Metal metal : Metals.all()) {
      METALS.put(metal, registerFluid(registry, metal.name, metal.meltTemp));
    }

    AIR = registerFluid(registry, "air", fluid -> fluid.setDensity(1).setViscosity(1).setGaseous(true));
  }

  private static Fluid registerFluid(final IForgeRegistry<Block> registry, final String name, final float meltTemp) {
    return registerFluid(registry, name, fluid -> fluid.setDensity(3000).setLuminosity(9).setViscosity(5000).setTemperature((int)(meltTemp + 273.15f)));
  }

  private static Fluid registerFluid(final IForgeRegistry<Block> registry, final String name, final Consumer<Fluid> fluidConfig) {
    final Fluid fluid;

    if(FluidRegistry.isFluidRegistered(name)) {
      fluid = FluidRegistry.getFluid(name);
    } else {
      fluid = new Fluid(name, GradientMod.resource("blocks/fluid_" + name), GradientMod.resource("blocks/fluid_" + name + "_flowing"));
      fluidConfig.accept(fluid);
      FluidRegistry.registerFluid(fluid);
      FluidRegistry.addBucketForFluid(fluid);
    }

    fluids.add(fluid);

    registry.register(new BlockMetalFluid(fluid));

    return fluid;
  }
}
