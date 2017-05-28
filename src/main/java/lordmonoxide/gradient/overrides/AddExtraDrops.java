package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AddExtraDrops {
  public static final AddExtraDrops instance = new AddExtraDrops();
  
  private AddExtraDrops() {
    MinecraftForge.addGrassSeed(new ItemStack(GradientItems.FIBRE), 10);
  }
  
  /**
   * Adds sticks as a random drop to leaves
   *
   * @param event
   */
  @SubscribeEvent
  public void leavesDropSticks(BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof BlockLeaves) {
      if(event.getWorld().rand.nextInt(10) == 0) {
        event.getDrops().add(new ItemStack(Items.STICK));
      }
    }
  }
  
  /**
   * Makes stones drop pebbles when broken by a hammer
   *
   * @param event
   */
  @SubscribeEvent
  public void stoneDropsPebbles(BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getMaterial() != Material.ROCK) {
      return;
    }
    
    if(event.getHarvester() == null) {
      return;
    }
    
    ItemStack hand = event.getHarvester().getHeldItemMainhand();
    
    if(hand == null) {
      return;
    }
    
    if(hand.getItem().getHarvestLevel(hand, "hammer", event.getHarvester(), event.getState()) != -1) { //$NON-NLS-1$
      event.getDrops().clear();
      event.setDropChance(0.5f);
      event.getDrops().add(new ItemStack(GradientBlocks.PEBBLE));
      event.getDrops().add(new ItemStack(GradientBlocks.PEBBLE));
      event.getDrops().add(new ItemStack(GradientBlocks.PEBBLE));
      event.getDrops().add(new ItemStack(GradientBlocks.PEBBLE));
    }
  }
}
