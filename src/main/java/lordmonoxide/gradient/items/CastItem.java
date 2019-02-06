package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;

public class CastItem extends GradientItem {
  public final GradientCasts.Cast cast;
  public final GradientMetals.Metal metal;

  public CastItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal) {
    super("cast_item." + cast.name + '.' + metal.name, CreativeTabs.TOOLS);
    this.cast = cast;
    this.metal = metal;
  }
}
