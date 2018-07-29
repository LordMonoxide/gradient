package lordmonoxide.gradient.items;

import lordmonoxide.gradient.*;
import lordmonoxide.gradient.init.CastRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CastItem extends GradientItem {
  private static final Set<CastItem> items = new HashSet<>();

  public static ItemStack getCastItem(final CastRegistry.Cast cast, final GradientMetals.Metal metal, final int amount) {
    final ItemStack stack = cast.itemForMetal(metal);

    if(stack != null) {
      return stack;
    }

    for(final CastItem item : items) {
      if(item.cast == cast && item.metal == metal) {
        return new ItemStack(item, amount);
      }
    }

    return ItemStack.EMPTY;
  }

  public final CastRegistry.Cast cast;
  public final GradientMetals.Metal metal;

  public CastItem(final CastRegistry.Cast cast, final GradientMetals.Metal metal) {
    super("cast_item." + cast.getRegistryName() + '.' + metal.name, CreativeTabs.TOOLS);
    items.add(this);

    this.cast = cast;
    this.metal = metal;
  }
}
