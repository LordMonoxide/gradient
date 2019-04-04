package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import net.minecraft.item.ItemGroup;

public class CastItem extends GradientItem {
  public final GradientCasts.Cast cast;
  public final GradientMetals.Metal metal;

  public CastItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal) {
    super("cast_item." + cast.name + '.' + metal.name, new Properties().group(ItemGroup.TOOLS));
    this.cast = cast;
    this.metal = metal;
  }
}
