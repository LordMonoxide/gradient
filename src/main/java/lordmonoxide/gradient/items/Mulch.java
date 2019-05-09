package lordmonoxide.gradient.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoneMeal;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mulch extends Item {
  public Mulch() {
    super(new Properties().group(ItemGroup.MATERIALS));
  }

  @Override
  public EnumActionResult onItemUse(final ItemUseContext context) {
    final EntityPlayer player = context.getPlayer();
    final EnumHand hand = player.getActiveHand();
    final ItemStack held = player.getHeldItem(hand);
    final BlockPos pos = context.getPos();
    final EnumFacing facing = context.getFace();
    final World world = context.getWorld();

    if(!player.canPlayerEdit(pos.offset(facing), facing, held)) {
      return EnumActionResult.FAIL;
    }

    if(ItemBoneMeal.applyBonemeal(held, world, pos, player)) {
      if(!world.isRemote()) {
        world.playEvent(2005, pos, 0);
      }

      return EnumActionResult.SUCCESS;
    }

    return EnumActionResult.PASS;
  }
}
