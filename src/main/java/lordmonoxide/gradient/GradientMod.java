package lordmonoxide.gradient;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GradientMod.MODID, name = GradientMod.NAME, version = GradientMod.VERSION, dependencies = "after:ic2")
public class GradientMod {
  public static final String MODID = "gradient";
  public static final String NAME = "Gradient";
  public static final String VERSION = "${version}";
  
  @Mod.Instance(MODID)
  public static GradientMod instance;
  
  @SidedProxy(serverSide = "lordmonoxide.gradient.CommonProxy", clientSide = "lordmonoxide.gradient.ClientProxy")
  public static CommonProxy proxy;
  
  static {
    FluidRegistry.enableUniversalBucket();
  }
  
  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) {
    System.out.println(NAME + " is loading!");
    
    proxy.preInit(event);
  }
  
  @Mod.EventHandler
  public void init(final FMLInitializationEvent event) {
    proxy.init(event);
  }
  
  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }
  
  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }
}
