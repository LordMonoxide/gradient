package lordmonoxide.gradient;

import lordmonoxide.gradient.overrides.BurningTooltips;
import lordmonoxide.gradient.overrides.CookingTooltips;
import lordmonoxide.gradient.overrides.MetalTooltips;
import lordmonoxide.gradient.overrides.OverrideInventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
    
    MinecraftForge.EVENT_BUS.register(BurningTooltips.instance);
    MinecraftForge.EVENT_BUS.register(CookingTooltips.instance);
    MinecraftForge.EVENT_BUS.register(MetalTooltips.instance);
    
    MinecraftForge.EVENT_BUS.register(KeyBindings.instance);
  }
}
