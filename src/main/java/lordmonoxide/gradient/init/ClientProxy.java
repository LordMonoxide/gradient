package lordmonoxide.gradient.init;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

import javax.annotation.Nullable;

public class ClientProxy implements IProxy {
  @Nullable
  @Override
  public IAnimationStateMachine loadAsm(final ResourceLocation loc, final ImmutableMap<String, ITimeValue> parameters) {
    return ModelLoaderRegistry.loadASM(loc, parameters);
  }
}
