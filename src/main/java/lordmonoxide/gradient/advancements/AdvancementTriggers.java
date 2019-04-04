package lordmonoxide.gradient.advancements;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class AdvancementTriggers {
  private AdvancementTriggers() { }

  public static final UsedHoeTrigger USED_HOE = new UsedHoeTrigger();
  public static final ChangeAgeTrigger CHANGE_AGE = new ChangeAgeTrigger();

  public static void register() {
    CriteriaTriggers.register(USED_HOE);
    CriteriaTriggers.register(CHANGE_AGE);
  }

  @SubscribeEvent
  public static void onUseHoe(final UseHoeEvent event) {
    if(!event.getContext().getWorld().isRemote) {
      USED_HOE.trigger((EntityPlayerMP)event.getEntityPlayer());
    }
  }
}
