package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public final class AddExtraDrops {
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
    
    if(hand.isEmpty()) {
      return;
    }
    
    if(hand.getItem().getHarvestLevel(hand, "hammer", event.getHarvester(), event.getState()) != -1) {
      List<ItemStack> drops = event.getDrops();
      
      drops.clear();
      
      int pebbleCount = event.getHarvester().getEntityWorld().rand.nextInt(4) + 2;
      
      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(GradientBlocks.PEBBLE));
      }
      
      ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());
      
      if(GradientMetals.instance.hasMeltable(metalStack)) {
        GradientMetals.Meltable meltable = GradientMetals.instance.getMeltable(metalStack);
        
        int nuggetCount = event.getHarvester().getEntityWorld().rand.nextInt((int)(meltable.amount * 4) + 1) + 2;
        
        for(int i = 0; i < nuggetCount; i++) {
          drops.add(meltable.metal.getNugget().copy());
        }
      }
    }
  }
}
