package lordmonoxide.gradient.utils;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public final class AgeUtils {
  private AgeUtils() { }

  public static Age getAgeNear(final Entity entity, final float distanceSquared) {
    Age age = Age.AGE1;

    for(final EntityPlayer player : entity.world.playerEntities) {
      if(entity.getDistanceSq(player) <= distanceSquared) {
        final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

        if(progress.getAge().compareTo(age) > 0) {
          age = progress.getAge();
        }
      }
    }

    return age;
  }
}
