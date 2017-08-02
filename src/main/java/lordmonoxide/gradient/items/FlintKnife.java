package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class FlintKnife extends GradientItemTool implements GradientCraftable {
  public FlintKnife() {
    super("flint_knife", 0, -1.0f, 3);
    this.setMaxDamage(49);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
      "F",
      "S",
      "W",
      'F', Items.FLINT,
      'S', "string",
      'W', "stickWood"
    ));
  }
}
