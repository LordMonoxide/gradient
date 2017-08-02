package lordmonoxide.gradient.recipes;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Iterator;
import java.util.List;

public class ShapedMetaAwareRecipe extends ShapedOreRecipe {
  public ShapedMetaAwareRecipe(Block result, Object... recipe) { this(new ItemStack(result), recipe); }
  public ShapedMetaAwareRecipe(Item  result, Object... recipe) { this(new ItemStack(result), recipe); }
  public ShapedMetaAwareRecipe(ItemStack result, Object... recipe) {
    super(result, recipe);
  }
  
  protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
    for(int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
      for(int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
        int subX = x - startX;
        int subY = y - startY;
        Object target = null;
        
        if(subX >= 0 && subY >= 0 && subX < this.width && subY < this.height) {
          target = this.input[(mirror ? this.width - subX - 1 : subX) + subY * this.width];
        }
        
        ItemStack slot = inv.getStackInRowAndColumn(x, y);
        
        if(target instanceof ItemStack) {
          if(!this.itemMatches((ItemStack)target, slot)) {
            return false;
          }
        } else if(target instanceof List) {
          boolean matched = false;
          
          Iterator<ItemStack> itr = ((List<ItemStack>)target).iterator();
          while(itr.hasNext() && !matched) {
            matched = this.itemMatches(itr.next(), slot);
          }
          
          if(!matched) {
            return false;
          }
        } else if(target == null && !slot.isEmpty()) {
          return false;
        }
      }
    }
    
    return true;
  }
  
  private boolean itemMatches(ItemStack a, ItemStack b) {
    if(!OreDictionary.itemMatches(a, b, false)) {
      return false;
    }
    
    if(a.hasTagCompound() != b.hasTagCompound()) {
      return false;
    }
    
    if(a.hasTagCompound()) {
      if(!a.getTagCompound().equals(b.getTagCompound())) {
        return false;
      }
    } else if(b.hasTagCompound()) {
      if(!b.getTagCompound().equals(a.getTagCompound())) {
        return false;
      }
    }
    
    return true;
  }
}
