package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BoneNeedle extends GradientItemTool implements GradientCraftable {
  public BoneNeedle() {
    super("bone_needle", 0, 0, 0);
    this.setMaxDamage(19);
  }
  
  @Override
  public void addRecipe() {
    //TODO
    /*GameRegistry.addRecipe(new ShapelessOreRecipe(
      this.getItemStack(4),
      "bone",
      "toolHammer"
    ));*/
  }
}
