package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.pebble.EntityPebble;
import lordmonoxide.gradient.overrides.BurningTooltips;
import lordmonoxide.gradient.overrides.CookingTooltips;
import lordmonoxide.gradient.overrides.MetalTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(final FMLPreInitializationEvent e) {
    super.preInit(e);
    
    MinecraftForge.EVENT_BUS.register(BurningTooltips.instance);
    MinecraftForge.EVENT_BUS.register(CookingTooltips.instance);
    MinecraftForge.EVENT_BUS.register(MetalTooltips.instance);
    
    MinecraftForge.EVENT_BUS.register(KeyBindings.instance);
  
    RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, manager -> new RenderSnowball<>(manager, ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), Minecraft.getMinecraft().getRenderItem()));
  }
}
