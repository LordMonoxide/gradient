package lordmonoxide.gradient.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.List;
import java.util.Random;

public class StoneMattock extends GradientItemWorldTool {
  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4, 2, 50);
    this.setHarvestLevel("axe", 0);
    this.setHarvestLevel("shovel", 0);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final IBlockState state = world.getBlockState(pos);

    // Handled in event handler; need this here to stop from placing items in offhand (see #541)
    if(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LOG2) {
      return EnumActionResult.SUCCESS;
    }

    return Items.STONE_HOE.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final EntityPlayer player, final EntityLivingBase entity, final EnumHand hand) {
    if(entity.world.isRemote) {
      return false;
    }

    if(entity instanceof IShearable) {
      final IShearable target = (IShearable)entity;
      final BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);

      if(target.isShearable(itemstack, entity.world, pos)) {
        itemstack.damageItem(1, entity);

        final List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

        if(drops.isEmpty()) {
          return true;
        }

        final Random rand = new Random();

        // Get one random drop from the list
        final ItemStack stack = drops.get(rand.nextInt(drops.size()));

        final EntityItem ent = entity.entityDropItem(stack, 1.0f);
        ent.motionY += rand.nextFloat() * 0.05F;
        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
      }

      return true;
    }

    return false;
  }
}
