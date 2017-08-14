package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.recipes.GradientCraftable;
import lordmonoxide.gradient.recipes.ShapelessMetaAwareRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Ingot extends GradientItem implements GradientCraftable {
  public Ingot() {
    super("ingot", CreativeTabs.MATERIALS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getIngot(final GradientMetals.Metal metal) {
    return GradientItems.INGOT.getItemStack(1, metal.id);
  }
  
  public static ItemStack getIngot(final GradientMetals.Metal metal, int amount) {
    return getIngot(metal);
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
    GradientMetals.metals.stream().map(Ingot::getIngot).forEach(list::add);
  }
  
  @Override
  public void addRecipe() {
    GradientMetals.metals.stream().map(metal -> new ShapelessMetaAwareRecipe(
      Ingot.getIngot(metal),
      GradientMetals.getBucket(metal),
      ItemClayCast.getCast(GradientCasts.INGOT)
    )).forEach(GameRegistry::addRecipe);
  }
}
