package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Purified extends GradientItem {
  private static final Map<GradientMetals.Metal, Purified> items = new HashMap<>();

  public static ItemStack get(final GradientMetals.Metal metal, final int amount) {
    final GradientItem item = items.get(metal);

    if(item == null) {
      return ItemStack.EMPTY;
    }

    return item.getItemStack(amount);
  }

  public final GradientMetals.Metal metal;

  public Purified(final GradientMetals.Metal metal) {
    super("purified." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
