package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Cloth extends GradientItem implements GradientItemCraftable {
  public Cloth() {
    super("cloth", CreativeTabs.MATERIALS);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
        "SSS",
        "SSS",
        "SSS",
        'S', "string"
    ));
  }
}
