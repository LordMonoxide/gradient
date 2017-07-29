package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.BlockMetalFluid;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
      
      GradientMetals.instance.metals.forEach(GradientFluids::registerFluidForMetal);
    }
  }
  
  private static void registerFluidForMetal(final GradientMetals.Metal metal) {
    final Fluid fluid;
    
    if(FluidRegistry.isFluidRegistered(metal.name)) {
      fluid = FluidRegistry.getFluid(metal.name);
    } else {
      fluid = new Fluid(metal.name, new ResourceLocation(GradientMod.MODID, "blocks/fluid_" + metal.name), new ResourceLocation(GradientMod.MODID, "blocks/fluid_" + metal.name + "_flowing"));
      FluidRegistry.registerFluid(fluid);
    }
    
    FluidRegistry.addBucketForFluid(fluid);
    
    Block block = new BlockMetalFluid(fluid);
    
    if(fluid.getBlock() == null) {
      fluid.setBlock(block);
    }
    
    GameRegistry.register(block);
    
    metal.fluid = fluid;
  }
}
