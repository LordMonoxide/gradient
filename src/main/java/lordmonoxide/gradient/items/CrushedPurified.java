package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrushedPurified extends GradientItem {
  private static final Map<GradientMetals.Metal, CrushedPurified> items = new HashMap<>();

  public static ItemStack get(final GradientMetals.Metal metal, final int amount) {
    final GradientItem item = items.get(metal);

    if(item == null) {
      return ItemStack.EMPTY;
    }

    return item.getItemStack(amount);
  }

  public final GradientMetals.Metal metal;

  public CrushedPurified(final GradientMetals.Metal metal) {
    super("crushed.purified." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
