package lordmonoxide.gradient.overrides;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DisableVanillaTools {
  public static final DisableVanillaTools instance = new DisableVanillaTools();

  private DisableVanillaTools() { }

  @SubscribeEvent
  public void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final ItemStack held = event.getEntityPlayer().getHeldItemMainhand();

    if(held.getItem() instanceof ItemTool) {
      event.setCanceled(true);
    }
  }
}
