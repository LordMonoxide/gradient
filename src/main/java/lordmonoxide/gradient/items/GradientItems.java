package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class GradientItems {
  public static final GradientItem test;
  
  static {
    test = register(new GradientItem("itemTest").setCreativeTab(CreativeTabs.MATERIALS));
  }
  
  private static <T extends Item> T register(T item) {
    GameRegistry.register(item);
    
    if (item instanceof GradientItem) {
      ((GradientItem)item).registerItemModel();
    }
    
    return item;
  }
}
