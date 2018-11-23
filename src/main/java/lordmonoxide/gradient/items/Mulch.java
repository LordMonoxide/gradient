package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mulch extends GradientItem {
  public Mulch() {
    super("mulch", CreativeTabs.MATERIALS);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final ItemStack held = player.getHeldItem(hand);

    if(!player.canPlayerEdit(pos.offset(facing), facing, held)) {
      return EnumActionResult.FAIL;
    }

    if(ItemDye.applyBonemeal(held, world, pos, player, hand)) {
      if(!world.isRemote) {
        world.playEvent(2005, pos, 0);
      }

      return EnumActionResult.SUCCESS;
    }

    return EnumActionResult.PASS;
  }
}
