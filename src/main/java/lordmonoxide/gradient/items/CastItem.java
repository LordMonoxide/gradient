package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.creativetab.CreativeTabs;

public class CastItem extends GradientItem {
  public final GradientCasts.Cast cast;
  public final Metal metal;

  public CastItem(final GradientCasts.Cast cast, final Metal metal) {
    super("cast_item." + cast.name + '.' + metal.name, CreativeTabs.TOOLS);
    this.cast = cast;
    this.metal = metal;
  }
}
