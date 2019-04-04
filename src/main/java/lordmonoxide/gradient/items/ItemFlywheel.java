package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.BlockFlywheel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
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

    return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState.with(BlockFlywheel.FACING, side.rotateY()));
  }

  private boolean isReplaceable(final IBlockState state, final BlockItemUseContext useContext) {
    final IWorld world = useContext.getWorld();
    final BlockPos pos = useContext.getPos();

    return world.getBlockState(pos).getBlock().isReplaceable(state, useContext);
  }
}
