package lordmonoxide.gradient.blocks.standingtorch;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemStandingTorch extends ItemBlock {
  public ItemStandingTorch(final BlockStandingTorch block) {
    super(block);
    this.setRegistryName(block.getRegistryName());
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      return EnumActionResult.SUCCESS;
    }

    if(facing != EnumFacing.UP) {
      return EnumActionResult.FAIL;
    }

    final IBlockState iblockstate = world.getBlockState(pos);
    final Block block = iblockstate.getBlock();
    final boolean bottomReplaceable = block.isReplaceable(world, pos);

    if(!bottomReplaceable) {
      pos = pos.up();
    }

    final BlockPos posTop = pos.up();
    final ItemStack held = player.getHeldItem(hand);

    if(player.canPlayerEdit(pos, facing, held) && player.canPlayerEdit(posTop, facing, held)) {
      final IBlockState topState = world.getBlockState(posTop);
      final boolean topReplaceable = topState.getBlock().isReplaceable(world, posTop);
      final boolean flag2 = bottomReplaceable || world.isAirBlock(pos);
      final boolean flag3 = topReplaceable || world.isAirBlock(posTop);

      if(flag2 && flag3) {
        final IBlockState standState = GradientBlocks.STANDING_TORCH.getDefaultState();
        world.setBlockState(pos, standState, 3);
        world.setBlockState(posTop, GradientBlocks.FIBRE_TORCH_UNLIT.getDefaultState(), 3);
        final SoundType sound = standState.getBlock().getSoundType(standState, world, pos, player);
        world.playSound(null, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);

        if(player instanceof EntityPlayerMP) {
          CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, held);
        }

        held.shrink(1);
        return EnumActionResult.SUCCESS;
      }
    }

    return EnumActionResult.FAIL;
  }
}
