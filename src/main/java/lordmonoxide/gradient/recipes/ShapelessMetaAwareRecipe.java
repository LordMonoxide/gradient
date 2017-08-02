package lordmonoxide.gradient.recipes;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Iterator;
import java.util.List;

public class ShapelessMetaAwareRecipe extends ShapelessOreRecipe {
  public ShapelessMetaAwareRecipe(Block result, Object... recipe){ this(new ItemStack(result), recipe); }
  public ShapelessMetaAwareRecipe(Item  result, Object... recipe){ this(new ItemStack(result), recipe); }
  public ShapelessMetaAwareRecipe(ItemStack result, Object... recipe) {
    super(result, recipe);
  }
  
  @Override
  public boolean matches(InventoryCrafting var1, World world) {
    NonNullList<Object> required = NonNullList.create();
    required.addAll(this.input);
    
    for(int x = 0; x < var1.getSizeInventory(); x++) {
      ItemStack slot = var1.getStackInSlot(x);
      
      if(!slot.isEmpty()) {
        boolean inRecipe = false;
        
        for(Object aRequired : required) {
          boolean match = false;
  
          if(aRequired instanceof ItemStack) {
            match = this.itemMatches((ItemStack)aRequired, slot);
          } else if(aRequired instanceof List) {
            Iterator<ItemStack> itr = ((List<ItemStack>)aRequired).iterator();
            while(itr.hasNext() && !match) {
              match = this.itemMatches(itr.next(), slot);
            }
          }
          
          if(match) {
            inRecipe = true;
            required.remove(aRequired);
            break;
          }
        }
        
        if(!inRecipe) {
          return false;
        }
      }
    }
    
    return required.isEmpty();
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
