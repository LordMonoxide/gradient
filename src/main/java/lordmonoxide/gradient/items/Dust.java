package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Dust extends GradientItem {
  public Dust() {
    super("dust", CreativeTabs.MATERIALS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getDust(final GradientMetals.Metal metal) {
    return GradientItems.DUST.getItemStack(1, metal.id);
  }
  
  public static ItemStack getDust(final GradientMetals.Metal metal, int amount) {
    return getDust(metal);
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
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    GradientMetals.metals.stream().map(Dust::getDust).forEach(list::add);
  }
}
