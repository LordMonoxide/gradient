package lordmonoxide.gradient.blocks.pebble;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemPebble extends ItemBlock {
  public ItemPebble(final Block block) {
    super(block);
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    final ItemStack stack = player.getHeldItem(hand);
    
    if(!player.capabilities.isCreativeMode) {
      stack.shrink(1);
    }
    
    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
    
    if(!world.isRemote) {
      final EntityPebble entity = new EntityPebble(world, player);
      entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
      world.spawnEntity(entity);
    }
    
    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
  }
}
