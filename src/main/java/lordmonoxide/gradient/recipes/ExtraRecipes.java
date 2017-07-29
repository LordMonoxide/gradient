package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
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
    
    for(GradientMetals.Alloy alloy : GradientMetals.instance.alloys) {
      GameRegistry.addShapelessRecipe(
        getBucket(alloy.output),
        alloy.inputs.stream().map(ExtraRecipes::getBucket).toArray()
      );
    }
  }
  
  private static ItemStack getBucket(GradientMetals.MetalStack metal) {
    ItemStack stack = getBucket(metal.metal);
    stack.grow(metal.amount - 1);
    
    return stack;
  }
  
  private static ItemStack getBucket(GradientMetals.Metal metal) {
    return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, metal.getFluid());
  }
}
