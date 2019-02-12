package lordmonoxide.gradient.core.events;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.items.CoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientCore.MODID)
public final class BlockDrops {
  private BlockDrops() { }

  @SubscribeEvent
  public static void leavesDropSticks(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof BlockLeaves) {
      if(event.getWorld().getRandom().nextInt(9) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(Items.STICK));
      }
    }
  }

  @SubscribeEvent
  public static void grassAndDirtDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final Block block = event.getState().getBlock();

    if(
      block == Blocks.GRASS ||
      block == Blocks.TALL_GRASS ||
      block == Blocks.DIRT ||
      block == Blocks.GRASS_BLOCK
    ) {
      if(event.getWorld().getRandom().nextInt(10) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(CoreItems.FIBRE));
      }
    }
  }

  @SubscribeEvent
  public static void wheatDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final IBlockState state = event.getState();

    if(state.getBlock() == Blocks.WHEAT) {
      final BlockCrops wheat = (BlockCrops)state.getBlock();

      if(state.get(BlockCrops.AGE) == wheat.getMaxAge()) {
        final int amount = event.getWorld().getRandom().nextInt(2 + event.getFortuneLevel());

        if(amount != 0) {
          event.getDrops().add(new ItemStack(CoreItems.FIBRE, amount));
        }
      }
    }
  }
}
