package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GradientItem extends Item {
  public GradientItem(final String name, final CreativeTabs creativeTab) {
    this.setUnlocalizedName(name);
    this.setRegistryName(name);
    this.setCreativeTab(creativeTab);
  }
  
  public ItemStack getItemStack(final int amount, final int meta) {
    return new ItemStack(this, amount, meta);
   }
  
  public ItemStack getItemStack(final int amount) {
    return new ItemStack(this, amount);
   }
  
  public ItemStack getItemStack() {
    return new ItemStack(this);
  }
}
