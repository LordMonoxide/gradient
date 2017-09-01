package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DustFlint extends GradientItem implements GradientCraftable {
  public DustFlint() {
    super("dust.flint", CreativeTabs.MATERIALS);
  }
  
  @Override
  public void addRecipe() {
    //TODO
    /*GameRegistry.addShapelessRecipe(
      this.getItemStack(),
      Items.FLINT,
      GradientItems.MORTAR
    );*/
  }
}
