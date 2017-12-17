package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Plate extends GradientItem {
  private static final Map<GradientMetals.Metal, Plate> items = new HashMap<>();
  
  public static ItemStack getPlate(final GradientMetals.Metal metal, final int amount) {
    return items.get(metal).getItemStack(amount);
  }
  
  public final GradientMetals.Metal metal;
  
  public Plate(final GradientMetals.Metal metal) {
    super("plate." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
