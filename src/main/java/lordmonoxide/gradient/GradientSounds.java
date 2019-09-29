package lordmonoxide.gradient;

import net.minecraft.util.ResourceLocation;
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
  public static SoundEvent FIRE_STARTER;
  public static SoundEvent GRINDSTONE;

  @SubscribeEvent
  public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
    GradientMod.logger.info("Registering sounds");

    BELLOWS_BLOW = sound("bellows_blow");
    FIRE_STARTER = sound("fire_starter");
    GRINDSTONE   = sound("grindstone");

    event.getRegistry().registerAll(
      BELLOWS_BLOW,
      FIRE_STARTER,
      GRINDSTONE
    );
  }

  private static SoundEvent sound(final String name) {
    final ResourceLocation loc = GradientMod.resource(name);
    return new SoundEvent(loc).setRegistryName(loc);
  }
}
