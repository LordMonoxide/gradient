package lordmonoxide.gradient;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
@GameRegistry.ObjectHolder(GradientMod.MODID)
public final class GradientSounds {
  private GradientSounds() { }

  public static final SoundEvent BELLOWS_BLOW = null;
  public static final SoundEvent FIRE_STARTER = null;
  public static final SoundEvent GRINDSTONE = null;

  @SubscribeEvent
  public static void registerSounds(final RegistryEvent.Register<SoundEvent> event) {
    GradientMod.logger.info("Registering sounds");

    event.getRegistry().registerAll(
      sound("bellows_blow"),
      sound("fire_starter"),
      sound("grindstone")
    );
  }

  private static SoundEvent sound(final String name) {
    final ResourceLocation loc = GradientMod.resource(name);
    return new SoundEvent(loc).setRegistryName(loc);
  }
}
