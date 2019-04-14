package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class HideBedding extends GradientItem {
  public HideBedding() {
    super("hide_bedding", CreativeTabs.TOOLS);
    this.setMaxDamage(4);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos usePos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      return EnumActionResult.PASS;
    }

    final BlockPos pos = player.getPosition();

    final WorldProvider.WorldSleepResult sleepResult = world.provider.canSleepAt(player, pos);
    if(sleepResult == WorldProvider.WorldSleepResult.BED_EXPLODES) {
      world.newExplosion(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 5.0f, true, true);
    }

    if(sleepResult == WorldProvider.WorldSleepResult.DENY) {
      return EnumActionResult.SUCCESS;
    }

    final EntityPlayer.SleepResult result = player.trySleep(pos);

    switch(result) {
      case OK:
        sleeping.add(player);
        player.getHeldItem(hand).damageItem(1, player);
        break;

      case NOT_POSSIBLE_NOW:
        player.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep"), true);
        break;

      case NOT_SAFE:
        player.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe"), true);
        break;

      case TOO_FAR_AWAY:
        player.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway"), true);
        break;
    }

    return EnumActionResult.SUCCESS;
  }

  private static final List<EntityPlayer> sleeping = new ArrayList<>();

  @SubscribeEvent
  public static void onSleepingLocationCheck(final SleepingLocationCheckEvent event) {
    if(sleeping.contains(event.getEntityPlayer())) {
      event.setResult(Event.Result.ALLOW);
    }
  }

  @SubscribeEvent
  public static void onWakeUp(final PlayerWakeUpEvent event) {
    sleeping.remove(event.getEntityPlayer());
  }
}
