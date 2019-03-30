package lordmonoxide.gradient.progress;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class PlayerProgressEvents {
  static Int2ObjectMap<Age> deferredAgeUpdates = new Int2ObjectAVLTreeMap<>();

  private PlayerProgressEvents() { }

  @SubscribeEvent
  public static void attachOnSpawn(final AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof EntityPlayer) {
      event.addCapability(CapabilityPlayerProgress.ID, new PlayerProgressProvider());
    }
  }

  @SubscribeEvent
  public static void onSpawn(final EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof EntityPlayer) {
      if(event.getWorld().isRemote) {
        final Age age = deferredAgeUpdates.remove(event.getEntity().getEntityId());

        if(age != null) {
          final PlayerProgress progress = event.getEntity().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

          if(progress != null) {
            progress.setAge(age);
          }
        }

        return;
      }

      PacketUpdatePlayerProgress.send((EntityPlayerMP)event.getEntity());
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
