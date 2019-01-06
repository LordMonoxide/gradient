package lordmonoxide.gradient.advancements;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class AdvancementTriggers {
  private AdvancementTriggers() { }

  public static final UsedHoeTrigger USED_HOE = new UsedHoeTrigger();

  public static void register() {
    CriteriaTriggers.register(USED_HOE);
  }

  @SubscribeEvent
  public static void onUseHoe(final UseHoeEvent event) {
    if(!event.getWorld().isRemote) {
      USED_HOE.trigger((EntityPlayerMP)event.getEntityPlayer());
    }
  }
}
