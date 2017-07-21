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
  
  @Override
  public int getMetadata(int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return super.getUnlocalizedName() + '.' + GradientMetals.instance.getMetal(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
    GradientMetals.instance.getMetals().forEach(ore -> list.add(this.getItemStack(1, ore.id)));
  }
}
