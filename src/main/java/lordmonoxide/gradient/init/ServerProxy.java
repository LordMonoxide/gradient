package lordmonoxide.gradient.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

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

  @Nullable
  @Override
  public IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters) {
    return null;
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
