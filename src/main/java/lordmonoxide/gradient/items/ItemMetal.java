package lordmonoxide.gradient.items;

import lordmonoxide.gradient.science.geology.Metal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemMetal extends GradientItem {
  private final String type;
  public final Metal metal;

  public ItemMetal(final String name, final Metal metal) {
    super(name + '.' + metal.name, CreativeTabs.MATERIALS);
    this.type = name;
    this.metal = metal;
  }

  @Override
  public String getItemStackDisplayName(ItemStack stack) {
    return I18n.translateToLocalFormatted("item.metal.type." + this.type, I18n.translateToLocal("metal." + this.metal.name));
  }
}
