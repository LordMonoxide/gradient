package lordmonoxide.gradient.progress;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class PlayerProgressEvents {
  private PlayerProgressEvents() { }

  @SubscribeEvent
  public static void attachOnSpawn(final AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof EntityPlayer) {
      event.addCapability(CapabilityPlayerProgress.ID, new PlayerProgressProvider());
    }
  }

  @SubscribeEvent
  public static void playerClone(final PlayerEvent.Clone event) {
    if(event.isWasDeath()) {
      final PlayerProgress newProgress = event.getEntityPlayer().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);
      final PlayerProgress oldProgress = event.getOriginal().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

      if(newProgress == null || oldProgress == null) {
        return;
      }

      newProgress.cloneFrom(oldProgress);
    }
  }
}
