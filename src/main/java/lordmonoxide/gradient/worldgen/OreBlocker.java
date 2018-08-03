package lordmonoxide.gradient.worldgen;

import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class OreBlocker {
  private OreBlocker() { }

  @SubscribeEvent
  public static void blockOreSpawn(final OreGenEvent.GenerateMinable event) {
    switch(event.getType()) {
      case COAL:
      case GOLD:
      case IRON:
      case LAPIS:
      case DIAMOND:
      case EMERALD:
      case REDSTONE:
        event.setResult(Event.Result.DENY);
        return;
    }
  }
}
