package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class MobSpawnEquipment {
  private MobSpawnEquipment() { }

  private static final float SPAWN_DISTANCE = 64.0f;

  @SubscribeEvent
  public static void removeSkeletonBows(final EntityJoinWorldEvent event) {
    if(!(event.getEntity() instanceof EntitySkeleton)) {
      return;
    }

    final EntitySkeleton skeleton = (EntitySkeleton)event.getEntity();

    if(AgeUtils.getAgeNear(skeleton, SPAWN_DISTANCE * SPAWN_DISTANCE).compareTo(Age.AGE1) <= 0) {
      skeleton.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
    }
  }
}
