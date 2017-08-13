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
  public ShapelessMetaAwareRecipe(final Block result, final Object... recipe){ this(new ItemStack(result), recipe); }
  public ShapelessMetaAwareRecipe(final Item  result, final Object... recipe){ this(new ItemStack(result), recipe); }
  public ShapelessMetaAwareRecipe(final ItemStack result, final Object... recipe) {
    super(result, recipe);
  }
  
  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    final NonNullList<Object> required = NonNullList.create();
    required.addAll(this.input);
    
    for(int x = 0; x < inv.getSizeInventory(); x++) {
      final ItemStack slot = inv.getStackInSlot(x);
      
      if(!slot.isEmpty()) {
        boolean inRecipe = false;
        
        for(final Object aRequired : required) {
          boolean match = false;
  
          if(aRequired instanceof ItemStack) {
            match = this.itemMatches((ItemStack)aRequired, slot);
          } else if(aRequired instanceof List) {
            final Iterator<ItemStack> itr = ((List<ItemStack>)aRequired).iterator();
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
  
  private boolean itemMatches(final ItemStack a, final ItemStack b) {
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
