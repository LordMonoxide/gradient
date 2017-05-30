package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.overrides.AddExtraDrops;
import lordmonoxide.gradient.overrides.DisableBreakingBlocksWithoutTools;
import lordmonoxide.gradient.overrides.DisableVanillaTools;
import lordmonoxide.gradient.overrides.GeneratePebbles;
import lordmonoxide.gradient.recipes.RecipeRemover;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    // Trigger loading
    System.out.println("Fibre: " + GradientItems.FIBRE);
    System.out.println("Pebble: " + GradientBlocks.PEBBLE);
    
    NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());
  }
  
  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(DisableVanillaTools.instance);
    MinecraftForge.EVENT_BUS.register(DisableBreakingBlocksWithoutTools.instance);
    MinecraftForge.EVENT_BUS.register(AddExtraDrops.instance);
    
    GameRegistry.registerWorldGenerator(new GeneratePebbles(), 0);
  
    RecipeRemover.remove();
    
    GradientBlocks.addRecipes();
    GradientItems.addRecipes();
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    
  }
}
