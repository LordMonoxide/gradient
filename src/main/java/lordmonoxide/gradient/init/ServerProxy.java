package lordmonoxide.gradient.init;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerProxy implements IProxy {
  @Override
  public void preInit(final FMLPreInitializationEvent event) {

  }

  @Override
  public void init(final FMLInitializationEvent event) {

  }

  @Override
  public void postInit(final FMLPostInitializationEvent event) {

  }

  @Override
  public ListenableFuture<Object> scheduleTask(final MessageContext ctx, final Runnable task) {
    return ctx.getServerHandler().player.getServerWorld().addScheduledTask(task);
  }

  @Override
  public World getWorld() {
    throw new UnsupportedOperationException("Can't get world statically on server side");
  }
}
