package lordmonoxide.gradient.recipes;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RecipeRemover {
  private RecipeRemover() { }
  
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
      //Blocks.PLANKS,
    };
    
    removeRecipes(tools);
    removeRecipes(items);
    removeRecipes(blocks);
    replacePlankRecipes();
  }
  
  private static void removeRecipes(Item[] items) {
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    List<Item> itemsList = Lists.newArrayList(items);
    
    while(it.hasNext()) {
      ItemStack stack = it.next().getRecipeOutput();
      
      if(!stack.isEmpty() && itemsList.contains(stack.getItem())) {
        it.remove();
      }
    }
  }
  
  private static void removeRecipes(Block[] blocks) {
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    List<Block> blocksList = Lists.newArrayList(blocks);
    
    while(it.hasNext()) {
      ItemStack stack = it.next().getRecipeOutput();
      
      if(!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
        if(blocksList.contains(((ItemBlock)stack.getItem()).getBlock())) {
          it.remove();
        }
      }
    }
  }
  
  private static void replacePlankRecipes() {
    // This would all be so much easier if logs weren't split up into LOG and LOG2
    
    List<IRecipe> toAdd = new ArrayList<>();
    
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
  
    while(it.hasNext()) {
      IRecipe recipe = it.next();
      
      ItemStack output = recipe.getRecipeOutput();
      
      // Is the result planks?
      if(!output.isEmpty() && output.getItem() instanceof ItemBlock) {
        if(((ItemBlock)output.getItem()).block instanceof BlockPlanks) {
          ItemStack component = ItemStack.EMPTY;
          
          // Grab the component so we can check if it's a single log
          if(recipe instanceof ShapelessRecipes) {
            List<ItemStack> components = ((ShapelessRecipes)recipe).recipeItems;
            
            if(components.size() == 1) {
              component = components.get(0);
            }
          } else if(recipe instanceof ShapedRecipes) {
            ItemStack[] components = ((ShapedRecipes)recipe).recipeItems;
            
            if(components.length == 1) {
              component = components[0];
            }
          }
          
          // Is the component logs?
          if(!component.isEmpty() && component.getItem() instanceof ItemBlock) {
            if(((ItemBlock)component.getItem()).block instanceof BlockLog) {
              // Add the new recipe
              toAdd.add(new ShapelessRecipes(
                new ItemStack(output.getItem(), 2, output.getMetadata()),
                Lists.newArrayList(component, new ItemStack(GradientItems.STONE_MATTOCK, 1, OreDictionary.WILDCARD_VALUE))
              ));
              
              // Remove the old one
              it.remove();
            }
          }
        }
      }
    }
    
    for(IRecipe recipe : toAdd) {
      GameRegistry.addRecipe(recipe);
    }
  }
}
