package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.config.GradientConfig;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Disables all vanilla ore spawning
 */
public final class DisableVanillaOre {
  private DisableVanillaOre() { }

  @SubscribeEvent
  public static void blockOreSpawn(final OreGenEvent.GenerateMinable event) {
    if(!GradientConfig.worldgen.disableVanillaOres) {
      return;
    }

    switch(event.getType()) {
      case COAL:
      case GOLD:
      case IRON:
      case LAPIS:
      case DIAMOND:
      case EMERALD:
      case REDSTONE:
        event.setResult(Event.Result.DENY);
    }
  }
}
