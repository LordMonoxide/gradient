package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.BlockMetalFluid;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

public final class GradientFluids {
  private GradientFluids() { }
  
  @Mod.EventBusSubscriber(modid = GradientMod.MODID)
  public static class RegistrationHandler {
    public static final Set<Fluid> FLUIDS = new HashSet<>();
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
      System.out.println("Registering fluids");
      
      GradientMetals.metals.forEach(metal -> registerFluidForMetal(event.getRegistry(), metal));
    }
  }
  
  private static void registerFluidForMetal(final IForgeRegistry<Block> registry, final GradientMetals.Metal metal) {
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
  }
}
