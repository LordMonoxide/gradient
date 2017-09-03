package lordmonoxide.gradient.recipes;

import com.google.gson.*;

public final class ExtraRecipes {
  private ExtraRecipes() { }
  
  private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  
  public static void addRecipes() {
    //TODO
    /*GameRegistry.addRecipe(new ShapedOreRecipe(
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
    
    GameRegistry.addRecipe(new ShapedOreRecipe(
      new ItemStack(Blocks.TORCH, 2),
      "C",
      "S",
      'C', "cloth",
      'S', "stickWood"
    ));
    
    GradientMetals.alloys.forEach(alloy -> GameRegistry.addShapelessRecipe(
      GradientMetals.getBucket(alloy.output),
      alloy.inputs.stream().map(GradientMetals::getBucket).toArray()
    ));*/
  }
}
