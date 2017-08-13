package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
    ));
  }
}
