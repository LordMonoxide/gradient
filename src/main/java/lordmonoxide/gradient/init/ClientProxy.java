package lordmonoxide.gradient.init;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.pebble.EntityPebble;
import lordmonoxide.gradient.overrides.BurningTooltips;
import lordmonoxide.gradient.overrides.MetalTooltips;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
  @Override
  public void preInit(final FMLPreInitializationEvent e) {
    MinecraftForge.EVENT_BUS.register(BurningTooltips.instance);
    MinecraftForge.EVENT_BUS.register(MetalTooltips.instance);

    RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, manager -> new RenderSnowball<>(manager, ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), Minecraft.getMinecraft().getRenderItem()));
  }

  @Override
  public void init(final FMLInitializationEvent event) {

  }

  @Override
  public void postInit(final FMLPostInitializationEvent event) {

  }
}
