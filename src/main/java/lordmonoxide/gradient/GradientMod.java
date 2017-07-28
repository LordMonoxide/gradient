package lordmonoxide.gradient;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GradientMod.MODID, name = GradientMod.NAME, version = GradientMod.VERSION)
public class GradientMod {
  public static final String MODID = "gradient";
  public static final String NAME = "Gradient";
  public static final String VERSION = "1.0";
  
  @Mod.Instance(MODID)
  public static GradientMod instance;
  
  @SidedProxy(serverSide = "lordmonoxide.gradient.CommonProxy", clientSide = "lordmonoxide.gradient.ClientProxy")
  public static CommonProxy proxy;
  
  static {
    FluidRegistry.enableUniversalBucket();
  }
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    System.out.println(NAME + " is loading!");
    
    proxy.preInit(event);
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }
}
