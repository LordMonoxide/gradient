package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;

public class ItemMetalOld extends GradientItem {
  public final GradientMetals.Metal metal;

  public ItemMetalOld(final String name, final GradientMetals.Metal metal) {
    super(name + '.' + metal.name, CreativeTabs.MATERIALS);
    this.metal = metal;
  }
}
