package lordmonoxide.gradient.items;

import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ItemMetal extends Item {
  public final Metal metal;

  public ItemMetal(final Metal metal) {
    super(new Properties().group(ItemGroup.MATERIALS));
    this.metal = metal;
  }
}
