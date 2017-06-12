package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class GradientItem extends Item {
  public GradientItem(String name, CreativeTabs creative_tab) {
    this.setUnlocalizedName(name);
    this.setRegistryName(name);
    this.setCreativeTab(creative_tab);
  }
}
