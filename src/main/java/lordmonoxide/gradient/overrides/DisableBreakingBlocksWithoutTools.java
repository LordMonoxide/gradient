package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DisableBreakingBlocksWithoutTools {
  private DisableBreakingBlocksWithoutTools() { }

  @SubscribeEvent
  public static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final IBlockState state = event.getState();

    if(state.getBlockHardness(event.getEntity().getEntityWorld(), event.getPos()) <= 1.0f || state.getBlock().getHarvestTool(state) == null) {
      return;
    }

    final ItemStack held = event.getEntityPlayer().getHeldItemMainhand();

    if(held.isEmpty()) {
      event.setCanceled(true);
      return;
    }

    if(!held.getItem().canHarvestBlock(held, event.getState())) {
      for(final ToolType toolType : held.getItem().getToolTypes(held)) {
        if(state.getBlock().isToolEffective(event.getState(), toolType)) {
          return;
        }
      }

      event.setCanceled(true);
    }
  }
}
