package lordmonoxide.gradient.age1.items;

import lordmonoxide.gradient.age1.GradientAge1;
import lordmonoxide.gradient.core.blocks.CoreBlocks;
import lordmonoxide.gradient.core.items.GradientToolType;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = GradientAge1.MODID)
public class ItemHammer extends ItemTool {
  public ItemHammer(final IItemTier tier, final float attackDamage, final float attackSpeed, final Item.Properties builder) {
    super(attackDamage, attackSpeed, tier, Set.of(), builder.addToolType(ToolType.PICKAXE, tier.getHarvestLevel()).addToolType(GradientToolType.HAMMER, tier.getHarvestLevel()));
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

    if(hand.getHarvestLevel(GradientToolType.HAMMER, event.getHarvester(), event.getState()) != -1) {
      final List<ItemStack> drops = event.getDrops();

      drops.clear();

      final Random rand = event.getHarvester().getEntityWorld().rand;

      final int pebbleCount = rand.nextInt(4) + 2;

      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(CoreBlocks.PEBBLE));
      }

      //TODO: metal nuggets
/*
      final ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());

      if(GradientMetals.hasMeltable(metalStack)) {
        final GradientMetals.Meltable meltable = GradientMetals.getMeltable(metalStack);

        final int nuggetCount = rand.nextInt(meltable.amount * 4 / 1000 * (event.getFortuneLevel() + 1) + 1) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(meltable.metal.getNugget());
        }
      }

      if(event.getState().getBlock() == Blocks.COAL_ORE) {
        final int nuggetCount = rand.nextInt(2 * (event.getFortuneLevel() + 1)) + 2;

        for(int i = 0; i < nuggetCount; i++) {
          drops.add(new ItemStack(GradientItems.NUGGET_COAL));
        }
      }
*/
    }
  }
}
