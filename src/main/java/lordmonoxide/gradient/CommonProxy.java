package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.overrides.*;
import lordmonoxide.gradient.recipes.ExtraRecipes;
import lordmonoxide.gradient.recipes.RecipeRemover;
import lordmonoxide.gradient.worldgen.OreGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
  public void preInit(final FMLPreInitializationEvent event) {
    System.out.println("------------------- PREINIT -------------------");
    
    MinecraftForge.EVENT_BUS.register(OverrideInventory.instance);
    
    NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());
  }
  
  @Mod.EventHandler
  public void init(final FMLInitializationEvent event) {
    System.out.println("------------------- INIT -------------------");
    
    MinecraftForge.EVENT_BUS.register(DisableVanillaTools.instance);
    MinecraftForge.EVENT_BUS.register(DisableBreakingBlocksWithoutTools.instance);
    MinecraftForge.EVENT_BUS.register(AddExtraDrops.instance);
    
    GameRegistry.registerWorldGenerator(new GeneratePebbles(), 0);
    GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
    
    RecipeRemover.remove();
    
    GradientBlocks.RegistrationHandler.addRecipes();
    GradientItems.RegistrationHandler.addRecipes();
    ExtraRecipes.addRecipes();
    
    GradientMetals.registerMeltables();
    
    GradientNet.register();
  }
  
  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    System.out.println("------------------- POSTINIT -------------------");
  }
}
