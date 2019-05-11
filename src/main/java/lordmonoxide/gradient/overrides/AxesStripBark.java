package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
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

    if(held.getItem().getHarvestLevel(held, ToolType.AXE, player, state) == -1) {
      return;
    }

    if(state.getBlock() instanceof BlockLog) {
      world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), getBark(state.getBlock())));
      held.damageItem(1, player);
      event.setUseItem(Event.Result.ALLOW);
    }
  }

  private static ItemStack getBark(final Block block) {
    return new ItemStack(
      block == Blocks.OAK_LOG    ? GradientItems.BARK_OAK :
      block == Blocks.SPRUCE_LOG ? GradientItems.BARK_SPRUCE :
      block == Blocks.BIRCH_LOG  ? GradientItems.BARK_BIRCH :
      block == Blocks.JUNGLE_LOG ? GradientItems.BARK_JUNGLE :
      block == Blocks.ACACIA_LOG ? GradientItems.BARK_ACACIA :
      GradientItems.BARK_DARK_OAK);
  }
}
