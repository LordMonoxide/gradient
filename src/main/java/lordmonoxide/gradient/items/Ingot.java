package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Ingot extends GradientItem {
  public Ingot() {
    super("ingot", CreativeTabs.MATERIALS);
    this.setHasSubtypes(true);
  }
  
  @SideOnly(Side.CLIENT)
  public ItemStack getIngot(final GradientMetals.Metal metal) {
    return this.getItemStack(1, metal.id);
  }
  
  @SideOnly(Side.CLIENT)
  public ItemStack getIngot(final GradientMetals.Metal metal, int amount) {
    return this.getIngot(metal);
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
    GradientMetals.metals.stream().filter(metal -> metal.canMakeIngots).map(this::getIngot).forEach(list::add);
  }
}
