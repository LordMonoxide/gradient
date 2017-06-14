package lordmonoxide.gradient.recipes;

import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class ExtraRecipes {
  private ExtraRecipes() { }
  
  public static void addRecipes() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      Items.LEATHER_HELMET,
      "LLL",
      "LNL",
      " S ",
      'L', "leather",
      'N', "needle",
      'S', "string"
    ));
    
    GameRegistry.addRecipe(new ShapedOreRecipe(
      Items.LEATHER_CHESTPLATE,
      "LNL",
      "LSL",
      "LLL",
      'L', "leather",
      'N', "needle",
      'S', "string"
    ));
    
    GameRegistry.addRecipe(new ShapedOreRecipe(
      Items.LEATHER_LEGGINGS,
      "LLL",
      "LNL",
      "LSL",
      'L', "leather",
      'N', "needle",
      'S', "string"
    ));
    
    GameRegistry.addRecipe(new ShapedOreRecipe(
      Items.LEATHER_BOOTS,
      "LNL",
      "LSL",
      'L', "leather",
      'N', "needle",
      'S', "string"
    ));
  }
}
