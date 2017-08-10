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
    GradientMetals.instance.metals.forEach(ore -> list.add(this.getItemStack(1, ore.id)));
  }
  
  @Override
  public void addRecipe() {
    final NonNullList<ItemStack> stacks = NonNullList.create();
    this.getSubItems(this, this.getCreativeTab(), stacks);
    
    for(final ItemStack stack : stacks) {
      final GradientMetals.Metal metal = GradientMetals.instance.getMetal(stack.getMetadata());
      
      GameRegistry.addRecipe(new ShapelessMetaAwareRecipe(
        stack,
        GradientMetals.getBucket(metal),
        ItemClayCast.getCast(GradientCasts.INGOT)
      ));
    }
  }
}
