package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemClayCastUnhardened extends ItemBlock {
  public ItemClayCastUnhardened(final Block block) {
    super(block);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    final ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);

    if(result.getType() != EnumActionResult.SUCCESS) {
      if(!world.isRemote) {
        final BlockPos pos = player.getPosition();
        player.openGui(GradientMod.instance, GradientGuiHandler.CLAY_CAST, world, pos.getX(), pos.getY(), pos.getZ());

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
      }
    }

    return result;
  }
}
