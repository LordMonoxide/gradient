package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Mortar extends GradientItem implements GradientCraftable {
  public Mortar() {
    super("mortar", CreativeTabs.TOOLS);
  }
  
  @Override
  public void addRecipe() {
    //TODO
    /*GameRegistry.addShapelessRecipe(
      this.getItemStack(),
      new ItemStack(GradientBlocks.CLAY_BOWL, 1, 1),
      GradientBlocks.PEBBLE
    );*/
  }
}
