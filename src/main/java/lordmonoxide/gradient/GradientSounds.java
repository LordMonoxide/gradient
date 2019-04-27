package lordmonoxide.gradient;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Dist.CLIENT)
public final class GradientSounds {
  private GradientSounds() { }

  public static SoundEvent BELLOWS_BLOW;

  @SubscribeEvent
  public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
    GradientMod.logger.info("Registering sounds");

    BELLOWS_BLOW = new SoundEvent(GradientMod.resource("bellows_blow"));

    event.getRegistry().register(BELLOWS_BLOW.setRegistryName(BELLOWS_BLOW.getName()));
  }
}
