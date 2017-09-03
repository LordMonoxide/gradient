package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.Tool;
import net.minecraftforge.common.crafting.IngredientNBT;

public class IngredientTool extends IngredientNBT {
  protected IngredientTool(GradientTools.Type type, GradientMetals.Metal metal) {
    super(Tool.getTool(type, metal));
  }
}
