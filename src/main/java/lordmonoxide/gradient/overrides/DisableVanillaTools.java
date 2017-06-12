package lordmonoxide.gradient.overrides;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DisableVanillaTools {
  public static final DisableVanillaTools instance = new DisableVanillaTools();
  
  private DisableVanillaTools() { }
  
  /**
   * Stops the event if the player is using an ItemTool
   *
   * @param event
   */
  @SubscribeEvent
  public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
    ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
    
    if(held.isEmpty()) {
      if(held.getItem() instanceof ItemTool) {
        event.setCanceled(true);
      }
    }
  }
}
