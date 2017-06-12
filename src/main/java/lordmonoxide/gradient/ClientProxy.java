package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.overrides.BurningTooltips;
import lordmonoxide.gradient.overrides.CookingTooltips;
import lordmonoxide.gradient.overrides.OverrideInventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
  
    MinecraftForge.EVENT_BUS.register(OverrideInventory.instance);
    MinecraftForge.EVENT_BUS.register(BurningTooltips.instance);
    MinecraftForge.EVENT_BUS.register(CookingTooltips.instance);
    
    GradientItems.addModels();
    GradientBlocks.addModels();
  }
}
