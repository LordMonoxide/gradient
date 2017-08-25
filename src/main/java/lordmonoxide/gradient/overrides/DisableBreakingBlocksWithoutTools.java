package lordmonoxide.gradient.overrides;

import net.minecraft.block.state.IBlockState;
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
  public void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final IBlockState state = event.getState();
    
    if(state.getBlockHardness(event.getEntity().getEntityWorld(), event.getPos()) <= 1.0f || state.getBlock().getHarvestTool(state) == null) {
      return;
    }
    
    final ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
    
    if(held.isEmpty()) {
      event.setCanceled(true);
      return;
    }
    
    if(!held.getItem().canHarvestBlock(event.getState(), held)) {
      for(final String toolClass : held.getItem().getToolClasses(held)) {
        if(state.getBlock().isToolEffective(toolClass, event.getState())) {
          return;
        }
      }
      
      event.setCanceled(true);
    }
  }
}
