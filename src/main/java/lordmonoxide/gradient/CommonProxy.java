package lordmonoxide.gradient;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    // Trigger loading
    System.out.println("Test: " + GradientItems.test);
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    
  }
}
