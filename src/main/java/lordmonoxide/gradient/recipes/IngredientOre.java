package lordmonoxide.gradient.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

/**
 * An oredict ingredient that supports wildcard oredict entries
 */
public class IngredientOre extends Ingredient {
  private static final ItemStack[] EMPTY_ITEMSTACK_ARRAY = {};

  public IngredientOre(final String ore) {
    super(OreDictionary.getOres(ore).toArray(EMPTY_ITEMSTACK_ARRAY));
  }
}
