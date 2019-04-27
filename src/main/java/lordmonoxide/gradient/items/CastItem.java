package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import net.minecraft.item.ItemGroup;
import lordmonoxide.gradient.science.geology.Metal;

public class CastItem extends GradientItem {
  public final GradientCasts.Cast cast;
  public final Metal metal;

  public CastItem(final GradientCasts.Cast cast, final Metal metal) {
    super("cast_item." + cast.name + '.' + metal.name, new Properties().group(ItemGroup.TOOLS));
    this.cast = cast;
    this.metal = metal;
  }
}
