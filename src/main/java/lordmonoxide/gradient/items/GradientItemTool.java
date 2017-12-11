package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class GradientItemTool extends GradientItem {
  public GradientItemTool(final String name, final CreativeTabs creativeTab, final int maxUses) {
    super(name, creativeTab);
    this.maxStackSize = 1;
    this.setMaxDamage(maxUses - 1);
  }
  
  public ItemStack getWildcardItemStack() {
    return this.getItemStack(1, OreDictionary.WILDCARD_VALUE);
  }
}
