package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class CastItem extends GradientItem {
  public final GradientCasts.Cast cast;
  public final Metal metal;

  public CastItem(final GradientCasts.Cast cast, final Metal metal) {
    super("cast_item." + cast.name + '.' + metal.name, CreativeTabs.TOOLS);
    this.cast = cast;
    this.metal = metal;
  }

  @Override
  public String getItemStackDisplayName(final ItemStack stack) {
    return I18n.translateToLocalFormatted("item.cast_item.name", I18n.translateToLocal("metal." + this.metal.name), I18n.translateToLocal("item.cast_item.type." + this.cast.name));
  }
}
