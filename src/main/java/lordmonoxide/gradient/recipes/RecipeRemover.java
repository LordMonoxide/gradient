package lordmonoxide.gradient.recipes;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.Iterator;
import java.util.List;

public class RecipeRemover {
  public static void remove() {
    Item[] tools = {
      Items.WOODEN_PICKAXE,
      Items.STONE_PICKAXE,
      Items.IRON_PICKAXE,
      Items.GOLDEN_PICKAXE,
      Items.DIAMOND_PICKAXE,
      
      Items.WOODEN_SWORD,
      Items.STONE_SWORD,
      Items.IRON_SWORD,
      Items.GOLDEN_SWORD,
      Items.DIAMOND_SWORD,
      
      Items.WOODEN_SHOVEL,
      Items.STONE_SHOVEL,
      Items.IRON_SHOVEL,
      Items.GOLDEN_SHOVEL,
      Items.DIAMOND_SHOVEL,
      
      Items.WOODEN_HOE,
      Items.STONE_HOE,
      Items.IRON_HOE,
      Items.GOLDEN_HOE,
      Items.DIAMOND_HOE,
      
      Items.WOODEN_AXE,
      Items.STONE_AXE,
      Items.IRON_AXE,
      Items.GOLDEN_AXE,
      Items.DIAMOND_AXE,
    };
    
    Item[] items = {
      Items.STICK,
    };
    
    Block[] blocks = {
      Blocks.PLANKS,
    };
    
    removeRecipes(tools);
    removeRecipes(items);
    removeRecipes(blocks);
  }
  
  private static void removeRecipes(Item[] items) {
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    List<Item> itemsList = Lists.newArrayList(items);
    
    while(it.hasNext()) {
      ItemStack stack = it.next().getRecipeOutput();
      
      if(stack != null && itemsList.contains(stack.getItem())) {
        it.remove();
      }
    }
  }
  
  private static void removeRecipes(Block[] blocks) {
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    List<Block> blocksList = Lists.newArrayList(blocks);
    
    while(it.hasNext()) {
      ItemStack stack = it.next().getRecipeOutput();
      
      if(stack != null && stack.getItem() instanceof ItemBlock) {
        if(blocksList.contains(((ItemBlock)stack.getItem()).getBlock())) {
          it.remove();
        }
      }
    }
  }
}
