package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Dust extends GradientItem {
  private static final Map<GradientMetals.Metal, Dust> items = new HashMap<>();
  
  public static ItemStack getDust(final GradientMetals.Metal metal, final int amount) {
    return items.get(metal).getItemStack(amount);
  }
  
  public final GradientMetals.Metal metal;
  
  public Dust(final GradientMetals.Metal metal) {
    super("dust." + metal.name, CreativeTabs.MATERIALS);
    items.put(metal, this);
    this.metal = metal;
  }
}
