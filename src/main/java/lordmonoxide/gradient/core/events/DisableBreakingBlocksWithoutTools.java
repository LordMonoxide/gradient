package lordmonoxide.gradient.core.events;

import lordmonoxide.gradient.core.GradientCore;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientCore.MODID)
public final class DisableBreakingBlocksWithoutTools {
  private DisableBreakingBlocksWithoutTools() { }

  @SubscribeEvent
  public static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final IBlockState state = event.getState();

    if(state.getBlockHardness(event.getEntity().getEntityWorld(), event.getPos()) <= 1.0f || state.getHarvestTool() == null) {
      return;
    }

    final ItemStack held = event.getEntityPlayer().getHeldItemMainhand();

    if(held.isEmpty()) {
      event.setCanceled(true);
      return;
    }

    if(!held.canHarvestBlock(event.getState())) {
      for(final ToolType type : held.getToolTypes()) {
        if(state.isToolEffective(type)) {
          return;
        }
      }

      event.setCanceled(true);
    }
  }
}
