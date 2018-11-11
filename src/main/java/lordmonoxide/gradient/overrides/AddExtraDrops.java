package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class AddExtraDrops {
  public static final AddExtraDrops instance = new AddExtraDrops();

  @GameRegistry.ObjectHolder("gradient:fibre")
  private static final Item FIBRE = null;

  @GameRegistry.ObjectHolder("minecraft:stick")
  private static final Item STICK = null;

  @GameRegistry.ObjectHolder("gradient:nugget.coal")
  private static final Item NUGGET_COAL = null;

  @GameRegistry.ObjectHolder("minecraft:gravel")
  private static final Block GRAVEL = null;

  @GameRegistry.ObjectHolder("gradient:pebble")
  private static final Block PEBBLE = null;

  @GameRegistry.ObjectHolder("minecraft:coal_ore")
  private static final Block COAL_ORE = null;

  @GameRegistry.ObjectHolder("biomesoplenty:grass")
  private static final Block BOP_GRASS = null;

  @GameRegistry.ObjectHolder("biomesoplenty:dirt")
  private static final Block BOP_DIRT = null;

  @SubscribeEvent
  public static void leavesDropSticks(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof BlockLeaves) {
      if(event.getWorld().rand.nextInt(9) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(STICK));
      }
    }
  }

  @SubscribeEvent
  public static void grassAndDirtDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final Block block = event.getState().getBlock();

    if(
      block == Blocks.TALLGRASS ||
      block == Blocks.DIRT ||
      block == Blocks.GRASS ||
      block == BOP_GRASS ||
      block == BOP_DIRT
    ) {
      if(event.getWorld().rand.nextInt(10) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(FIBRE));
      }
    }
  }

  @SubscribeEvent
  public static void wheatDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final IBlockState state = event.getState();

    if(state.getBlock() == Blocks.WHEAT) {
      final BlockCrops wheat = (BlockCrops)state.getBlock();

      if(state.getValue(BlockCrops.AGE) == wheat.getMaxAge()) {
        final int amount = event.getWorld().rand.nextInt(2 + event.getFortuneLevel());

        if(amount != 0) {
          event.getDrops().add(new ItemStack(FIBRE, amount));
        }
      }
    }
  }

  @SubscribeEvent
  public static void gravelDropsPebbles(final BlockEvent.HarvestDropsEvent event) {
    final IBlockState state = event.getState();

    if(state.getBlock() == GRAVEL) {
      for(int i = 0; i < 3 + event.getFortuneLevel(); i++) {
        if(event.getWorld().rand.nextInt(10) == 0) {
          event.getDrops().add(new ItemStack(PEBBLE));
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

    final ItemStack hand = event.getHarvester().getHeldItemMainhand();

    if(hand.isEmpty()) {
      return;
    }

    if(hand.getItem().getHarvestLevel(hand, "hammer", event.getHarvester(), event.getState()) != -1) {
      final List<ItemStack> drops = event.getDrops();

      drops.clear();

      final Random rand = event.getHarvester().getEntityWorld().rand;

      final int pebbleCount = rand.nextInt(4) + 2;

      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(PEBBLE));
      }

      final ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());

      if(GradientMetals.hasMeltable(metalStack)) {
        final GradientMetals.Meltable meltable = GradientMetals.getMeltable(metalStack);

        final int nuggetCount = rand.nextInt(meltable.amount * 4 / 1000 * (event.getFortuneLevel() + 1) + 1) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(meltable.metal.getNugget());
        }
      }

      if(event.getState().getBlock() == COAL_ORE) {
        final int nuggetCount = rand.nextInt(2 * (event.getFortuneLevel() + 1)) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(new ItemStack(NUGGET_COAL));
        }
      }
    }
  }
}
