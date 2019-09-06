package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.hacks.FixToolBreakingNotFiringHarvestDropEvents;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.recipes.MeltingRecipe;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class AddExtraDrops {
  private AddExtraDrops() { }

  @GameRegistry.ObjectHolder("biomesoplenty:grass")
  private static final Block BOP_GRASS = null;

  @GameRegistry.ObjectHolder("biomesoplenty:dirt")
  private static final Block BOP_DIRT = null;

  @SubscribeEvent
  public static void leavesDropSticks(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof BlockLeaves) {
      if(event.getWorld().rand.nextInt(9) < 1 + event.getFortuneLevel()) {
        event.getDrops().add(new ItemStack(Items.STICK));
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
        event.getDrops().add(new ItemStack(GradientItems.FIBRE));
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
          event.getDrops().add(new ItemStack(GradientItems.FIBRE, amount));
        }
      }
    }
  }

  @SubscribeEvent
  public static void gravelDropsPebbles(final BlockEvent.HarvestDropsEvent event) {
    final IBlockState state = event.getState();

    if(state.getBlock() == Blocks.GRAVEL) {
      for(int i = 0; i < 3 + event.getFortuneLevel(); i++) {
        if(event.getWorld().rand.nextInt(10) == 0) {
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

    final EntityPlayer player = event.getHarvester();

    final FixToolBreakingNotFiringHarvestDropEvents.IPlayerItem cap = player.getCapability(FixToolBreakingNotFiringHarvestDropEvents.CapabilityPlayerItem.CAPABILITY, null);
    final ItemStack hand = cap != null ? cap.getStack() : player.getHeldItemMainhand();

    if(hand.isEmpty()) {
      return;
    }

    if(hand.getItem().getHarvestLevel(hand, "hammer", event.getHarvester(), event.getState()) != -1) {
      final List<ItemStack> drops = event.getDrops();

      drops.clear();

      final Random rand = event.getHarvester().getEntityWorld().rand;

      final int pebbleCount = rand.nextInt(4) + 2;

      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(GradientBlocks.PEBBLE));
      }

      final ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());
      final MeltingRecipe meltable = RecipeUtils.findRecipe(MeltingRecipe.class, recipe -> recipe.matches(metalStack));

      if(meltable != null) {
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
