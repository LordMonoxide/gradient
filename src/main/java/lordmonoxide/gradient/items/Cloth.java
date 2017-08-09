package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Cloth extends GradientItem implements GradientCraftable {
  public Cloth() {
    super("cloth", CreativeTabs.MATERIALS);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
        "SS",
        "SS",
        'S', "string"
    ));
  }
}
