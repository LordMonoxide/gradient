package lordmonoxide.gradient.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GradientItems {
  private static final List<Item> items = new ArrayList<>();
  
  public static final GradientItem FIBRE = register(new Fibre());
  
  @SideOnly(Side.CLIENT)
  public static void addModels() {
    for(Item item : items) {
      if(!item.getHasSubtypes()) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory")); //$NON-NLS-1$
      } else {
        List<ItemStack> stacks = new ArrayList<>();
        item.getSubItems(item, null, stacks);
        
        if(item instanceof ItemBlock) {
          for(ItemStack stack : stacks) {
            ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory")); //$NON-NLS-1$
          }
        } else {
          for(ItemStack stack : stacks) {
            ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getUnlocalizedName(stack).substring(5), "inventory")); //$NON-NLS-1$
          }
        }
      }
    }
  }
  
  private static <T extends Item> T register(T item) {
    items.add(item);
    GameRegistry.register(item);
    
    return item;
  }
}
