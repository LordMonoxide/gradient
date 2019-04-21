package lordmonoxide.gradient;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
public final class GradientSounds {
  private GradientSounds() { }

  public static SoundEvent BELLOWS_BLOW;

  @SubscribeEvent
  public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
    GradientMod.logger.info("Registering sounds");

    BELLOWS_BLOW = new SoundEvent(GradientMod.resource("bellows_blow"));

    event.getRegistry().register(BELLOWS_BLOW.setRegistryName(BELLOWS_BLOW.getSoundName()));
  }
}
