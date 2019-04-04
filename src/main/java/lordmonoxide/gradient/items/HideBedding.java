package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeDimension;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class HideBedding extends GradientItem {
  public HideBedding() {
    super("hide_bedding", new Properties().group(ItemGroup.TOOLS));
  }

  @Override
  public EnumActionResult onItemUse(final ItemUseContext context) {
    final World world = context.getWorld();

    if(world.isRemote()) {
      return EnumActionResult.PASS;
    }

    final EntityPlayer player = context.getPlayer();

    if(player == null) {
      return EnumActionResult.PASS;
    }

    final BlockPos pos = player.getPosition();

    final IForgeDimension.SleepResult sleepResult = world.getDimension().canSleepAt(player, pos);
    if(sleepResult == IForgeDimension.SleepResult.BED_EXPLODES) {
      world.createExplosion(null, DamageSource.netherBedExplosion(), pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 5.0f, true, true);
    }

    if(sleepResult == IForgeDimension.SleepResult.DENY) {
      return EnumActionResult.SUCCESS;
    }

    final EntityPlayer.SleepResult result = player.trySleep(pos);

    switch(result) {
      case OK:
        sleeping.add(player);
        final EnumHand hand = context.getPlayer().getActiveHand();
        player.setHeldItem(hand, ItemStack.EMPTY);
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
