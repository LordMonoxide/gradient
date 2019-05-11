package lordmonoxide.gradient.config;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ConfigEventHandler {
  private ConfigEventHandler() { }

  @SubscribeEvent
  public static void onConfigChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if(event.getModID().equals(GradientMod.MOD_ID)) {
      //TODO ConfigManager.sync(GradientMod.MODID, Config.Type.INSTANCE);
    }
  }
}
