package lordmonoxide.gradient.progress;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.network.PacketUpdatePlayerProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public final class PlayerProgressEvents {
  private static final Int2ObjectMap<Age> deferredAgeUpdates = new Int2ObjectAVLTreeMap<>();

  private PlayerProgressEvents() { }

  @SubscribeEvent
  public static void attachOnSpawn(final AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof PlayerEntity) {
      event.addCapability(CapabilityPlayerProgress.ID, new PlayerProgressProvider());
    }
  }

  @SubscribeEvent
  public static void onSpawn(final EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof PlayerEntity) {
      if(event.getWorld().isRemote) {
        final Age age = deferredAgeUpdates.remove(event.getEntity().getEntityId());

        if(age != null) {
          event.getEntity()
            .getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY)
            .ifPresent(progress -> progress.setAge(age));
        }

        return;
      }

      PacketUpdatePlayerProgress.send((ServerPlayerEntity)event.getEntity());
    }
  }

  @SubscribeEvent
  public static void playerClone(final PlayerEvent.Clone event) {
    if(event.isWasDeath()) {
      //TODO: nulls
      final PlayerProgress newProgress = event.getEntityPlayer().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null).orElse(null);
      final PlayerProgress oldProgress = event.getOriginal().getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null).orElse(null);

      if(newProgress == null || oldProgress == null) {
        return;
      }

      newProgress.cloneFrom(oldProgress);
    }
  }

  public static void deferAgeUpdate(final int id, final Age age) {
    deferredAgeUpdates.put(id, age);
  }
}
