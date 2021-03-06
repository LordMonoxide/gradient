package lordmonoxide.gradient.utils;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgress;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;

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

  public static Age getPlayerAge(final EntityLivingBase player) {
    final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress != null) {
      return progress.getAge();
    }

    return Age.AGE1;
  }

  public static boolean playerMeetsAgeRequirement(final EntityLivingBase player, final Age age) {
    final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress != null) {
      return progress.meetsAgeRequirement(age);
    }

    return false;
  }

  public static boolean playerMeetsAgeRequirement(final InventoryCrafting inv, final Age age) {
    final EntityPlayer player = RecipeUtils.findPlayerFromInv(inv);

    if(player != null) {
      return playerMeetsAgeRequirement(player, age);
    }

    return false;
  }
}
