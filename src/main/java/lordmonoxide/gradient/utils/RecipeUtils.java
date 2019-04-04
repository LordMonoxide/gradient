package lordmonoxide.gradient.utils;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.function.Predicate;

public final class RecipeUtils {
  private RecipeUtils() { }

  private static final Field eventHandlerField;
  private static final Field containerPlayerPlayerField;
  private static final Field slotCraftingPlayerField;

  static {
    try {
      eventHandlerField = InventoryCrafting.class.getDeclaredField(ObfuscationReflectionHelper.remapName("field_70465_c"));
      containerPlayerPlayerField = ContainerPlayer.class.getDeclaredField(ObfuscationReflectionHelper.remapName("field_82862_h"));
      slotCraftingPlayerField = ContainerPlayer.class.getDeclaredField(ObfuscationReflectionHelper.remapName("field_75238_b"));
    } catch(final NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T extends Container> T getContainer(final InventoryCrafting inv, final Class<T> containerClass) {
    try {
      final Object container = eventHandlerField.get(inv);
      return containerClass.isInstance(container) ? containerClass.cast(eventHandlerField.get(inv)) : null;
    } catch(final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static EntityPlayer findPlayerFromInv(final InventoryCrafting inv) {
    try {
      final Container container = getContainer(inv, Container.class);

      if(container instanceof ContainerPlayer) {
        return (EntityPlayer)containerPlayerPlayerField.get(container);
      }

      if(container instanceof ContainerWorkbench) {
        return (EntityPlayer)slotCraftingPlayerField.get(container.getSlot(0));
      }

      // Can't find player
      return null;
    } catch(final Throwable e) {
      GradientMod.logger.error("Failed to get player from inv", e);
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T extends IRecipe> T findRecipe(final Class<T> recipeClass, final Predicate<T> match) {
    for(final IRecipe recipe : GradientMod.getRecipeManager().getRecipes()) {
      if(recipeClass.isInstance(recipe)) {
        final T cast = recipeClass.cast(recipe);

        if(match.test(cast)) {
          return recipeClass.cast(recipe);
        }
      }
    }

    return null;
  }
}
