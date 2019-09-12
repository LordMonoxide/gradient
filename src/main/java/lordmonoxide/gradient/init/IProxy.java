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

public interface IProxy {
  void preInit(final FMLPreInitializationEvent event);
  void init(final FMLInitializationEvent event);
  void postInit(final FMLPostInitializationEvent event);
  @Nullable
  IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters);
  ListenableFuture<Object> scheduleTask(final MessageContext ctx, final Runnable task);
  World getWorld();
}
