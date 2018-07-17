package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Nugget extends GradientItem {
  private static final Map<GradientMetals.Metal, Nugget> items = new HashMap<>();

  public static ItemStack get(final GradientMetals.Metal metal, final int amount) {
    return items.get(metal).getItemStack(amount);
  }

  public static ItemStack get(final GradientMetals.Metal metal) {
    return items.get(metal).getItemStack();
  }

  public final GradientMetals.Metal metal;

  public Nugget(final GradientMetals.Metal metal) {
    super("nugget." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
