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
  
  @Override
  public int getMetadata(int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return super.getUnlocalizedName() + '.' + GradientMetals.instance.get(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
    GradientMetals.instance.get().forEach((a, ore) -> list.add(this.getItemStack(1, ore.id)));
  }
}
