package lordmonoxide.gradient.items;

import net.minecraft.item.ItemGroup;
import lordmonoxide.gradient.science.geology.Metal;

public class ItemMetal extends GradientItem {
  public final Metal metal;

  public ItemMetal(final String name, final Metal metal) {
    super(name + '.' + metal.name, new Properties().group(ItemGroup.MATERIALS));
    this.metal = metal;
  }
}
