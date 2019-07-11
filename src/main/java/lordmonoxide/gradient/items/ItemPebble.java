package lordmonoxide.gradient.items;

import lordmonoxide.gradient.entities.PebbleEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemPebble extends BlockItem {
  public ItemPebble(final Block block) {
    super(block, new Properties().group(ItemGroup.MATERIALS));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
    final ItemStack stack = player.getHeldItem(hand);

    if(!player.isCreative()) {
      stack.shrink(1);
    }

    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f));

    if(!world.isRemote) {
      final PebbleEntity entity = new PebbleEntity(world, player);
      entity.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f);
      world.addEntity(entity);
    }

    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }
}
