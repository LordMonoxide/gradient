package lordmonoxide.gradient.recipes;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.Tool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
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
import java.util.function.Function;

public final class RecipeRemover {
  private RecipeRemover() { }
  
  private static final Container DUMMY_CONTAINER = new Container() {
    @Override
    public boolean canInteractWith(EntityPlayer player) {
      return true;
    }
  };
  
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
      
      Items.LEATHER_HELMET,
      Items.LEATHER_CHESTPLATE,
      Items.LEATHER_LEGGINGS,
      Items.LEATHER_BOOTS,
    };
    
    Item[] items = {
      Items.STICK,
      Items.field_191525_da, // IRON_NUGGET
      Items.IRON_INGOT,
      Items.GOLD_NUGGET,
      Items.GOLD_INGOT,
    };
    
    Block[] blocks = {
      Blocks.FURNACE,
    };
    
    removeRecipes(tools);
    removeRecipes(items);
    removeRecipes(blocks);
    replacePlankRecipes();
    removeStringToWoolRecipes();
    removeTorchRecipeUsingCoal();
  }
  
  private static void removeRecipes(final Item[] items) {
    final List<Item> list = Lists.newArrayList(items);
    removeRecipes(stack -> !stack.isEmpty() && list.contains(stack.getItem()));
  }
  
  private static void removeRecipes(final Block[] blocks) {
    final List<Block> list = Lists.newArrayList(blocks);
    removeRecipes(stack -> !stack.isEmpty() && stack.getItem() instanceof ItemBlock && list.contains(((ItemBlock)stack.getItem()).getBlock()));
  }
  
  private static <T> void removeRecipes(final Function<ItemStack, Boolean> predicate) {
    final Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
  
    while(it.hasNext()) {
      final ItemStack stack = it.next().getRecipeOutput();
      
      if(predicate.apply(stack)) {
        it.remove();
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
              
              for(GradientMetals.Metal metal : GradientMetals.metals) {
                ItemStack tool = Tool.getTool(GradientTools.MATTOCK, metal);
                tool.setItemDamage(OreDictionary.WILDCARD_VALUE);
                
                toAdd.add(new ShapelessMetaAwareRecipe(
                  new ItemStack(output.getItem(), 2, output.getMetadata()),
                  component,
                  tool
                ));
              }
              
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
  
  private static void removeStringToWoolRecipes() {
    int removed = 0;
    
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    while(it.hasNext()) {
      IRecipe recipe = it.next();
      
      ItemStack output = recipe.getRecipeOutput();
      
      if(output.getItem() instanceof ItemBlock) {
        if(((ItemBlock)output.getItem()).block == Blocks.WOOL) {
          InventoryCrafting inv = new InventoryCrafting(DUMMY_CONTAINER, 2, 2);
          inv.setInventorySlotContents(0, new ItemStack(Items.STRING));
          inv.setInventorySlotContents(1, new ItemStack(Items.STRING));
          inv.setInventorySlotContents(2, new ItemStack(Items.STRING));
          inv.setInventorySlotContents(3, new ItemStack(Items.STRING));
          
          if(recipe.matches(inv, null)) {
            it.remove();
            removed++;
          }
        }
      }
    }
    
    if(removed == 0) {
      System.out.println("Failed to remove wool recipes!");
    } else {
      System.out.println("Removed " + removed + " wool recipes!");
    }
  }
  
  private static void removeTorchRecipeUsingCoal() {
    int removed = 0;
    
    Iterator<IRecipe> it = CraftingManager.getInstance().getRecipeList().iterator();
    
    while(it.hasNext()) {
      IRecipe recipe = it.next();
      
      ItemStack output = recipe.getRecipeOutput();
      
      if(output.getItem() instanceof ItemBlock) {
        if(((ItemBlock)output.getItem()).block instanceof BlockTorch) {
          InventoryCrafting inv = new InventoryCrafting(DUMMY_CONTAINER, 1, 2);
          inv.setInventorySlotContents(0, new ItemStack(Items.COAL));
          inv.setInventorySlotContents(1, new ItemStack(Items.STICK));
          
          if(recipe.matches(inv, null)) {
            it.remove();
            removed++;
          }
          
          inv.setInventorySlotContents(0, new ItemStack(Items.COAL, 1, 1));
          
          if(recipe.matches(inv, null)) {
            it.remove();
            removed++;
          }
        }
      }
    }
    
    if(removed == 0) {
      System.out.println("Failed to remove torch recipes!");
    } else {
      System.out.println("Removed " + removed + " torch recipes!");
    }
  }
}
