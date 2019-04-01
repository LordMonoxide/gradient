package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictHelper {
  private OreDictHelper() { }

  public static ItemStack getFirst(final String name) {
    return getFirst(name, GradientMod.MODID);
  }

  public static ItemStack getFirst(final String name, final int size) {
    return getFirst(name, size, GradientMod.MODID);
  }

  public static ItemStack getFirst(final String name, final int size, final String preferredModId) {
    final NonNullList<ItemStack> ores = OreDictionary.getOres(name);

    if(ores.isEmpty()) {
      return ItemStack.EMPTY;
    }

    for(final ItemStack ore : ores) {
      if(preferredModId.equals(ore.getItem().getRegistryName().getNamespace())) {
        if(size == 0) {
          return ore.copy();
        }

        return ItemHandlerHelper.copyStackWithSize(ore, size);
      }
    }

    return ores.get(0).copy();
  }

  public static ItemStack getFirst(final String name, final String preferredModId) {
    return getFirst(name, 0, preferredModId);
  }
}
