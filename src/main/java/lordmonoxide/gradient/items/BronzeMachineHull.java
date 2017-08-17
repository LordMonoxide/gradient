package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BronzeMachineHull extends GradientItem implements GradientCraftable {
  public BronzeMachineHull() {
    super("bronze_machine_hull", CreativeTabs.MATERIALS);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(
      new ItemStack(this),
      "PPP",
      "P P",
      "PPP",
      'P', Plate.getPlate(GradientMetals.getMetal("bronze"))
    );
  }
}
