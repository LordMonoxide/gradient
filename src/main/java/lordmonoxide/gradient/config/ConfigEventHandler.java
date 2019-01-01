package lordmonoxide.gradient.config;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class ConfigEventHandler {
  private ConfigEventHandler() { }

  @SubscribeEvent
  public static void onConfigChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
    if(event.getModID().equals(GradientMod.MODID)) {
      ConfigManager.sync(GradientMod.MODID, Config.Type.INSTANCE);
    }
  }
}
