package lordmonoxide.gradient.items;

import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.creativetab.CreativeTabs;

public class ItemMetal extends GradientItem {
  public final Metal metal;

  public ItemMetal(final String name, final Metal metal) {
    super(name + '.' + metal.name, CreativeTabs.MATERIALS);
    this.metal = metal;
  }
}
