package lordmonoxide.gradient.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mulch extends Item {
  public Mulch() {
    super(new Properties().group(ItemGroup.MATERIALS));
  }

  @Override
  public ActionResultType onItemUse(final ItemUseContext context) {
    final PlayerEntity player = context.getPlayer();
    final Hand hand = player.getActiveHand();
    final ItemStack held = player.getHeldItem(hand);
    final BlockPos pos = context.getPos();
    final Direction facing = context.getFace();
    final World world = context.getWorld();

    if(!player.canPlayerEdit(pos.offset(facing), facing, held)) {
      return ActionResultType.FAIL;
    }

    if(BoneMealItem.applyBonemeal(held, world, pos, player)) {
      if(!world.isRemote()) {
        world.playEvent(2005, pos, 0);
      }

      return ActionResultType.SUCCESS;
    }

    return ActionResultType.PASS;
  }
}
