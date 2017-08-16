package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
  
  @SubscribeEvent
  public void leavesDropSticks(final BlockEvent.HarvestDropsEvent event) {
    if(event.getState().getBlock() instanceof BlockLeaves) {
      if(event.getWorld().rand.nextInt(7) == 0) {
        event.getDrops().add(new ItemStack(Items.STICK));
      }
    }
  }
  
  @SubscribeEvent
  public void wheatDropsFibre(final BlockEvent.HarvestDropsEvent event) {
    final IBlockState state = event.getState();
    
    if(state.getBlock() == Blocks.WHEAT) {
      final BlockCrops wheat = (BlockCrops)state.getBlock();
      
      if(state.getValue(BlockCrops.AGE) == wheat.getMaxAge()) {
        if(event.getWorld().rand.nextInt(2) == 0) {
          event.getDrops().add(GradientItems.FIBRE.getItemStack());
        }
      }
    }
  }
  
  @SubscribeEvent
  public void stoneDropsPebbles(final BlockEvent.HarvestDropsEvent event) {
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
  
      final int pebbleCount = event.getHarvester().getEntityWorld().rand.nextInt(4) + 2;
      
      for(int i = 0; i < pebbleCount; i++) {
        drops.add(new ItemStack(GradientBlocks.PEBBLE));
      }
  
      final ItemStack metalStack = event.getState().getBlock().getItem(event.getWorld(), event.getPos(), event.getState());
      
      if(GradientMetals.hasMeltable(metalStack)) {
        final GradientMetals.Meltable meltable = GradientMetals.getMeltable(metalStack);
  
        final int nuggetCount = event.getHarvester().getEntityWorld().rand.nextInt(meltable.amount * 4 / 1000 + 1) + 2;
        
        for(int i = 0; i < nuggetCount; i++) {
          drops.add(meltable.metal.getNugget());
        }
      }
    }
  }
}
