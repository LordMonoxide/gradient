package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class StoneHammer extends GradientItemTool implements GradientCraftable {
  public StoneHammer() {
    super("stone_hammer", 0.5f, -2.4f, 2);
    this.setHarvestLevel("pickaxe", 2);
    this.setHarvestLevel("hammer", 2);
    this.setMaxDamage(19);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
      "P",
      "F",
      "S",
      'P', GradientBlocks.PEBBLE,
      'F', "string",
      'S', "stickWood"
    ));
  }
}
