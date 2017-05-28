package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.inventory.OverrideInventory;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
  
    MinecraftForge.EVENT_BUS.register(OverrideInventory.instance);
    
    GradientItems.addModels();
    GradientBlocks.addModels();
  }
}
