package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Igniter extends GradientItem implements GradientCraftable {
  public Igniter() {
    super("igniter", CreativeTabs.MATERIALS);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapelessOreRecipe(
      this,
      Items.FLINT,
      "dustMagnesium"
    ));
  }
}
