package lordmonoxide.gradient.recipes;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.Tool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
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
    final Item[] tools = {
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
    
    final Item[] items = {
      Items.STICK,
      Items.IRON_NUGGET,
      Items.IRON_INGOT,
      Items.GOLD_NUGGET,
      Items.GOLD_INGOT,
    };
    
    final Block[] blocks = {
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
    final Iterator<IRecipe> it = CraftingManager.REGISTRY.iterator();
  
    while(it.hasNext()) {
      final ItemStack stack = it.next().getRecipeOutput();
      
      if(predicate.apply(stack)) {
        it.remove();
      }
    }
  }
  
  private static void replacePlankRecipes() {
    final List<IRecipe> toAdd = new ArrayList<>();
    
    final Iterator<IRecipe> it = CraftingManager.REGISTRY.iterator();
    
    int removed = 0;
    
    while(it.hasNext()) {
      final IRecipe recipe = it.next();
      
      final ItemStack output = recipe.getRecipeOutput();
      
      outerLoop:
      for(final ItemStack stackPlankBlock : OreDictionary.getOres("plankWood")) {
        if(!(stackPlankBlock.getItem() instanceof ItemBlock)) {
          continue;
        }
        
        final Block blockPlank = ((ItemBlock)stackPlankBlock.getItem()).getBlock();
        
        for(final IBlockState statePlank : blockPlank.getBlockState().getValidStates()) {
          final ItemStack stackPlank = stackPlankBlock.copy();
          stackPlank.setItemDamage(blockPlank.getMetaFromState(statePlank));
          
          if(output.isItemEqual(stackPlank)) {
            for(final ItemStack stackLogBlock : OreDictionary.getOres("logWood")) {
              if(!(stackLogBlock.getItem() instanceof ItemBlock)) {
                continue;
              }
              
              final Block blockLog = ((ItemBlock)stackLogBlock.getItem()).getBlock();
              
              for(final IBlockState stateLog : blockLog.getBlockState().getValidStates()) {
                final ItemStack stackLog = stackLogBlock.copy();
                stackLog.setItemDamage(blockLog.getMetaFromState(stateLog));
                
                final InventoryCrafting inv = new InventoryCrafting(DUMMY_CONTAINER, 2, 2);
                inv.setInventorySlotContents(0, stackLog);
                
                if(recipe.matches(inv, null)) {
                  toAdd.add(new ShapelessRecipes(
                    new ItemStack(output.getItem(), 2, output.getMetadata()),
                    Lists.newArrayList(stackLog, new ItemStack(GradientItems.STONE_MATTOCK, 1, OreDictionary.WILDCARD_VALUE))
                  ));
                  
                  for(final GradientMetals.Metal metal : GradientMetals.metals) {
                    final ItemStack tool = Tool.getTool(GradientTools.MATTOCK, metal);
                    tool.setItemDamage(OreDictionary.WILDCARD_VALUE);
                    
                    toAdd.add(new ShapelessMetaAwareRecipe(
                      new ItemStack(output.getItem(), 2, output.getMetadata()),
                      stackLog,
                      tool
                    ));
                  }
                  
                  it.remove();
                  
                  removed++;
                  
                  break outerLoop;
                }
              }
            }
          }
        }
      }
    }
    
    for(final IRecipe recipe : toAdd) {
      GameRegistry.addRecipe(recipe);
    }
    
    if(removed == 0) {
      System.out.println("Failed to replaced plank recipes!");
    } else {
      System.out.println("Replaced " + removed + " plank recipes!");
    }
  }
  
  private static void removeStringToWoolRecipes() {
    int removed = 0;
    
    Iterator<IRecipe> it = CraftingManager.REGISTRY.iterator();
    
    while(it.hasNext()) {
      IRecipe recipe = it.next();
      
      ItemStack output = recipe.getRecipeOutput();
      
      if(output.getItem() instanceof ItemBlock) {
        if(((ItemBlock)output.getItem()).getBlock() == Blocks.WOOL) {
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
    
    Iterator<IRecipe> it = CraftingManager.REGISTRY.iterator();
    
    while(it.hasNext()) {
      IRecipe recipe = it.next();
      
      ItemStack output = recipe.getRecipeOutput();
      
      if(output.getItem() instanceof ItemBlock) {
        if(((ItemBlock)output.getItem()).getBlock() instanceof BlockTorch) {
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
