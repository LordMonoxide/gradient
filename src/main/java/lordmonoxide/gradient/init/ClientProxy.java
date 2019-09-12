package lordmonoxide.gradient.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ListenableFuture;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.entities.EntityPebble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class ClientProxy implements IProxy {
  @Override
  public void preInit(final FMLPreInitializationEvent e) {
    RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, manager -> new RenderSnowball<>(manager, ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), Minecraft.getMinecraft().getRenderItem()));
  }

  @Override
  public void init(final FMLInitializationEvent event) {

  }

  @Override
  public void postInit(final FMLPostInitializationEvent event) {

  }

  @Nullable
  @Override
  public IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters) {
    return ModelLoaderRegistry.loadASM(loc, parameters);
  }

  @Override
  public ListenableFuture<Object> scheduleTask(final MessageContext ctx, final Runnable task) {
    return Minecraft.getMinecraft().addScheduledTask(task);
  }

  @Override
  public World getWorld() {
    return Minecraft.getMinecraft().world;
  }
}
