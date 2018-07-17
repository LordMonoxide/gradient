package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AlloyNugget extends GradientItem {
  private static final Map<GradientMetals.Metal, AlloyNugget> items = new HashMap<>();

  public static ItemStack get(final GradientMetals.Metal metal, final int amount) {
    return items.get(metal).getItemStack(amount);
  }

  public static ItemStack get(final GradientMetals.Metal metal) {
    return items.get(metal).getItemStack();
  }

  public final GradientMetals.Metal metal;

  public AlloyNugget(final GradientMetals.Metal metal) {
    super("alloy_nugget." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
