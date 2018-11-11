package lordmonoxide.gradient.overrides;

import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class DisableVanillaTools {
  public static final DisableVanillaTools instance = new DisableVanillaTools();

  private DisableVanillaTools() { }

  @SubscribeEvent
  public void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final Item held = event.getEntityPlayer().getHeldItemMainhand().getItem();

    if(held instanceof ItemTool || held instanceof ItemHoe || held instanceof ItemSword) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onUseHoe(final UseHoeEvent event) {
    final Item held = event.getCurrent().getItem();

    if(held instanceof ItemHoe) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public void onAttackEntity(final AttackEntityEvent event) {
    final Item held = event.getEntityPlayer().getHeldItemMainhand().getItem();

    if(held instanceof ItemTool || held instanceof ItemHoe || held instanceof ItemSword) {
      event.setCanceled(true);
    }
  }
}
