package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.hacks.FixToolBreakingNotFiringHarvestDropEvents;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.GradientToolTypes;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public final class AddExtraDrops {
  private AddExtraDrops() { }

  @ObjectHolder("biomesoplenty:grass")
  private static Block BOP_GRASS;

  @ObjectHolder("biomesoplenty:dirt")
  private static Block BOP_DIRT;

  @SubscribeEvent
  public static void leavesDropSticks(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof LeavesBlock) {
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
      block == Blocks.FERN ||
      block == Blocks.LARGE_FERN ||
      block == Blocks.DIRT ||
      block == Blocks.GRASS_BLOCK ||
      block == BOP_GRASS ||
      block == BOP_DIRT
    ) {
      if(event.getWorld().getRandom().nextInt(10) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(GradientItems.FIBRE));
      }
    }
  }

  @SubscribeEvent
  public static void wheatDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final BlockState state = event.getState();

    if(state.getBlock() == Blocks.WHEAT) {
      final CropsBlock wheat = (CropsBlock)state.getBlock();

      if(state.get(CropsBlock.AGE) == wheat.getMaxAge()) {
        final int amount = event.getWorld().getRandom().nextInt(2 + event.getFortuneLevel());

        if(amount != 0) {
          event.getDrops().add(new ItemStack(GradientItems.FIBRE, amount));
        }
      }
    }
  }

  @SubscribeEvent
  public static void gravelDropsPebbles(final BlockEvent.HarvestDropsEvent event) {
    final BlockState state = event.getState();

    if(state.getBlock() == Blocks.GRAVEL) {
      for(int i = 0; i < 3 + event.getFortuneLevel(); i++) {
        if(event.getWorld().getRandom().nextInt(10) == 0) {
          event.getDrops().add(new ItemStack(GradientBlocks.PEBBLE));
        }
      }
    }
  }

  @SubscribeEvent
  public static void stoneDropsPebbles(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getMaterial() != Material.ROCK) {
      return;
    }

    if(event.getHarvester() == null) {
      return;
    }

    final PlayerEntity player = event.getHarvester();

    //TODO: null
    final FixToolBreakingNotFiringHarvestDropEvents.IPlayerItem cap = player.getCapability(FixToolBreakingNotFiringHarvestDropEvents.CapabilityPlayerItem.CAPABILITY, null).orElse(null);

    final ItemStack hand = cap != null ? cap.getStack() : player.getHeldItemMainhand();

    if(hand.isEmpty()) {
      return;
    }

    if(hand.getItem().getHarvestLevel(hand, GradientToolTypes.HAMMER, event.getHarvester(), event.getState()) != -1) {
      final List<ItemStack> drops = event.getDrops();

      drops.clear();

      final Random rand = event.getHarvester().getEntityWorld().rand;

      final int pebbleCount = rand.nextInt(4) + 2;

      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(GradientBlocks.PEBBLE));
      }

      final ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());
      final Meltable meltable = Meltables.get(metalStack);

      if(meltable != Meltables.INVALID_MELTABLE) {
        final Metal metal = Metals.get(meltable);
        final int nuggetCount = rand.nextInt(meltable.amount * 4 / 1000 * (event.getFortuneLevel() + 1) + 1) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(new ItemStack(GradientItems.nugget(metal)));
        }
      }

      if(event.getState().getBlock() == Blocks.COAL_ORE) {
        final int nuggetCount = rand.nextInt(2 * (event.getFortuneLevel() + 1)) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(new ItemStack(GradientItems.NUGGET_COAL));
        }
      }
    }
  }
}
