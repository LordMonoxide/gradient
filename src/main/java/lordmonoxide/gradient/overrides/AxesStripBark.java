package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class AxesStripBark {
  private AxesStripBark() { }

  @SubscribeEvent
  public static void onRightClick(final PlayerInteractEvent.RightClickBlock event) {
    final World world = event.getWorld();

    if(world.isRemote) {
      return;
    }

    final EntityPlayer player = event.getEntityPlayer();
    final ItemStack held = player.getHeldItemMainhand();

    final BlockPos pos = event.getPos();
    final IBlockState state = world.getBlockState(pos);

    if(held.getItem().getHarvestLevel(held, "axe", player, state) == -1) {
      return;
    }

    if(state.getBlock() == Blocks.LOG) {
      final BlockPlanks.EnumType variant = state.getValue(BlockOldLog.VARIANT);
      final EnumFacing.Axis axis = getAxis(state);

      final Block stripped =
        variant == BlockPlanks.EnumType.OAK    ? GradientBlocks.STRIPPED_OAK_WOOD :
        variant == BlockPlanks.EnumType.SPRUCE ? GradientBlocks.STRIPPED_SPRUCE_WOOD :
        variant == BlockPlanks.EnumType.BIRCH  ? GradientBlocks.STRIPPED_BIRCH_WOOD :
        GradientBlocks.STRIPPED_JUNGLE_WOOD;

      world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), getBark(variant)));
      world.setBlockState(pos, stripped.getDefaultState().withProperty(BlockRotatedPillar.AXIS, axis), 3);
      held.damageItem(1, player);
      event.setUseItem(Event.Result.ALLOW);

      return;
    }

    if(state.getBlock() == Blocks.LOG2) {
      final BlockPlanks.EnumType variant = state.getValue(BlockNewLog.VARIANT);
      final EnumFacing.Axis axis = getAxis(state);

      final Block stripped =
        variant == BlockPlanks.EnumType.ACACIA ? GradientBlocks.STRIPPED_ACACIA_WOOD :
        GradientBlocks.STRIPPED_DARK_OAK_WOOD;

      world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), getBark(variant)));
      world.setBlockState(pos, stripped.getDefaultState().withProperty(BlockRotatedPillar.AXIS, axis), 3);
      held.damageItem(1, player);
      event.setUseItem(Event.Result.ALLOW);

      return;
    }
  }

  private static EnumFacing.Axis getAxis(final IBlockState state) {
    switch(state.getValue(BlockLog.LOG_AXIS)) {
      case X:
        return EnumFacing.Axis.X;

      case Y:
        return EnumFacing.Axis.Y;

      case Z:
        return EnumFacing.Axis.Z;

      default:
        return EnumFacing.Axis.Y;
    }
  }

  private static ItemStack getBark(final BlockPlanks.EnumType variant) {
    return new ItemStack(
      variant == BlockPlanks.EnumType.OAK    ? GradientItems.BARK_OAK :
      variant == BlockPlanks.EnumType.SPRUCE ? GradientItems.BARK_SPRUCE :
      variant == BlockPlanks.EnumType.BIRCH  ? GradientItems.BARK_BIRCH :
      variant == BlockPlanks.EnumType.JUNGLE ? GradientItems.BARK_JUNGLE :
      variant == BlockPlanks.EnumType.ACACIA ? GradientItems.BARK_ACACIA :
      GradientItems.BARK_DARK_OAK);
  }
}
