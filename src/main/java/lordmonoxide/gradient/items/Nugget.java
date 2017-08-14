package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Nugget extends GradientItem {
  public Nugget() {
    super("nugget", CreativeTabs.MATERIALS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getNugget(final GradientMetals.Metal metal) {
    return GradientItems.NUGGET.getItemStack(1, metal.id);
  }
  
  public static ItemStack getNugget(final GradientMetals.Metal metal, int amount) {
    return getNugget(metal);
  }
  
  @Override
  public int getMetadata(final int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName() + '.' + GradientMetals.getMetal(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    GradientMetals.metals.stream().map(Nugget::getNugget).forEach(list::add);
  }
}
