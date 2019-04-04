package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DisableVanillaTools {
  private DisableVanillaTools() { }

  @SubscribeEvent
  public static void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
    final Item held = event.getEntityPlayer().getHeldItemMainhand().getItem();

    if(held instanceof ItemTool || held instanceof ItemHoe || held instanceof ItemSword) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onUseHoe(final UseHoeEvent event) {
    final Item held = event.getContext().getItem().getItem();

    if(held instanceof ItemHoe) {
      event.setCanceled(true);
    }
  }

  @SubscribeEvent
  public static void onAttackEntity(final AttackEntityEvent event) {
    final Item held = event.getEntityPlayer().getHeldItemMainhand().getItem();

    if(held instanceof ItemTool || held instanceof ItemHoe || held instanceof ItemSword) {
      event.setCanceled(true);
    }
  }
}
