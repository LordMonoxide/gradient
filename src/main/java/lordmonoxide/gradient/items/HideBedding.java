package lordmonoxide.gradient.items;

import com.mojang.datafixers.util.Either;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeDimension;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public class HideBedding extends Item {
  public HideBedding() {
    super(new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(4));
  }

  @Override
  public ActionResultType onItemUse(final ItemUseContext context) {
    final World world = context.getWorld();

    if(world.isRemote()) {
      return ActionResultType.PASS;
    }

    final PlayerEntity player = context.getPlayer();

    if(player == null) {
      return ActionResultType.PASS;
    }

    final BlockPos pos = player.getPosition();

    final IForgeDimension.SleepResult sleepResult = world.getDimension().canSleepAt(player, pos);
    if(sleepResult == IForgeDimension.SleepResult.BED_EXPLODES) {
      world.createExplosion(null, DamageSource.netherBedExplosion(), pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 5.0f, true, Explosion.Mode.DESTROY);
    }

    if(sleepResult == IForgeDimension.SleepResult.DENY) {
      return ActionResultType.SUCCESS;
    }

    final Either<PlayerEntity.SleepResult, Unit> trySleep = player.trySleep(pos);

    trySleep.ifLeft(result -> {
      switch(result) {
        case NOT_POSSIBLE_NOW:
          player.sendStatusMessage(new TranslationTextComponent("tile.bed.noSleep"), true);
          break;

        case NOT_SAFE:
          player.sendStatusMessage(new TranslationTextComponent("tile.bed.notSafe"), true);
          break;

        case TOO_FAR_AWAY:
          player.sendStatusMessage(new TranslationTextComponent("tile.bed.tooFarAway"), true);
          break;
      }
    }).ifRight(unit -> {
      sleeping.add(player);
      context.getItem().damageItem(1, player, e -> e.sendBreakAnimation(e.getActiveHand()));
    });

    return ActionResultType.SUCCESS;
  }

  private static final List<PlayerEntity> sleeping = new ArrayList<>();

  @SubscribeEvent
  public static void onSleepingLocationCheck(final SleepingLocationCheckEvent event) {
    if(sleeping.contains(event.getEntity())) {
      event.setResult(Event.Result.ALLOW);
    }
  }

  @SubscribeEvent
  public static void onWakeUp(final PlayerWakeUpEvent event) {
    sleeping.remove(event.getEntityPlayer());
  }
}
