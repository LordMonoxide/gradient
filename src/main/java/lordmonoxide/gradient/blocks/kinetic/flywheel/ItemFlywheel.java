package lordmonoxide.gradient.blocks.kinetic.flywheel;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlywheel extends ItemBlock {
  public ItemFlywheel(final BlockFlywheel block) {
    super(block);
  }

  @Override
  public boolean placeBlockAt(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ, final IBlockState newState) {
    if(side == EnumFacing.UP || side == EnumFacing.DOWN) {
      return false;
    }

    final BlockPos left = pos.offset(side.rotateY());
    final BlockPos right = pos.offset(side.rotateYCCW());

    if(
      !this.isReplaceable(world, pos.up()) ||
      !this.isReplaceable(world, pos.down()) ||
      !this.isReplaceable(world, left) ||
      !this.isReplaceable(world, left.up()) ||
      !this.isReplaceable(world, left.down()) ||
      !this.isReplaceable(world, right) ||
      !this.isReplaceable(world, right.up()) ||
      !this.isReplaceable(world, right.down())
    ) {
      return false;
    }

    return
      this.placeStructureBlock(stack, player, world, pos.up(), EnumFacing.DOWN, hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, pos.down(), EnumFacing.UP, hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, left, side.rotateYCCW(), hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, left.up(), EnumFacing.DOWN, hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, left.down(), EnumFacing.UP, hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, right, side.rotateY(), hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, right.up(), EnumFacing.DOWN, hitX, hitY, hitZ) &&
      this.placeStructureBlock(stack, player, world, right.down(), EnumFacing.UP, hitX, hitY, hitZ) &&
      super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
  }

  private boolean placeStructureBlock(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return super.placeBlockAt(stack, player, world, pos, facing.getOpposite(), hitX, hitY, hitZ, GradientBlocks.FLYWHEEL_STRUCTURE.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, 0, player, EnumHand.MAIN_HAND));
  }

  private boolean isReplaceable(final World world, final BlockPos pos) {
    return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
  }
}
