package lordmonoxide.gradient.overrides;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DisableBreakingBlocksWithoutTools {
  public static final DisableBreakingBlocksWithoutTools instance = new DisableBreakingBlocksWithoutTools();
  
  private DisableBreakingBlocksWithoutTools() { }
  
  /**
   * Stops a player from breaking the block with the wrong tool or if it has a hardness > 1
   *
   * @param event
   */
  @SubscribeEvent
  public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
    if(event.getState().getBlockHardness(event.getEntity().getEntityWorld(), event.getPos()) <= 1.0f) {
      return;
    }
    
    ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
    
    if(held.isEmpty() || !held.getItem().canHarvestBlock(event.getState())) {
      event.setCanceled(true);
    }
  }
}
