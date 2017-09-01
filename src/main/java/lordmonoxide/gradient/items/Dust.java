package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.commons.lang3.StringUtils;

public class Dust extends GradientItem implements GradientCraftable {
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
  
  @Override
  public void addRecipe() {
    //TODO
    /*for(GradientMetals.Metal metal : GradientMetals.metals) {
      if(metal.hardness <= 2.5f) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(
          this.getItemStack(1, metal.id),
          "ore" + StringUtils.capitalize(metal.name),
          GradientItems.MORTAR
        ));
      }
    }*/
  }
}
