package lordmonoxide.gradient.init;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy {
  void preInit(final FMLPreInitializationEvent event);
  void init(final FMLInitializationEvent event);
  void postInit(final FMLPostInitializationEvent event);
  ListenableFuture<Object> scheduleTask(final MessageContext ctx, final Runnable task);
  World getWorld();
}
