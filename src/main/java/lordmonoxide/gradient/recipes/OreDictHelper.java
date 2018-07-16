package lordmonoxide.gradient.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictHelper {
  private OreDictHelper() { }

  public static ItemStack getFirst(final String name) {
    return OreDictionary.getOres(name, false).iterator().next().copy();
  }

  public static ItemStack getFirst(final String name, final int size) {
    return ItemHandlerHelper.copyStackWithSize(OreDictionary.getOres(name, false).iterator().next(), size);
  }
}
