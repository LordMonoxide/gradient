package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class CastItem extends Item {
  public final GradientCasts.Cast cast;
  public final Metal metal;

  public CastItem(final GradientCasts.Cast cast, final Metal metal) {
    super(new Properties().group(ItemGroup.TOOLS));
    this.cast = cast;
    this.metal = metal;
  }
}
