package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.item.crafting.IRecipe;

public interface GradientRecipe extends IRecipe {
  Age getAge();
}
