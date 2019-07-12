package lordmonoxide.gradient.utils;

import cpw.mods.modlauncher.api.INameMappingService;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.function.Predicate;

public final class RecipeUtils {
  private RecipeUtils() { }

  private static final Field eventHandlerField;
  private static final Field containerPlayerPlayerField;
  private static final Field slotCraftingPlayerField;

  //TODO: use AT

  static {
    try {
      eventHandlerField = CraftingInventory.class.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_70465_c"));
      eventHandlerField.setAccessible(true);
      containerPlayerPlayerField = PlayerContainer.class.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_82862_h"));
      containerPlayerPlayerField.setAccessible(true);
      slotCraftingPlayerField = PlayerContainer.class.getDeclaredField(ObfuscationReflectionHelper.remapName(INameMappingService.Domain.FIELD, "field_75238_b"));
      slotCraftingPlayerField.setAccessible(true);
    } catch(final NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <T extends Container> T getContainer(final CraftingInventory inv, final Class<T> containerClass) {
    try {
      final Object container = eventHandlerField.get(inv);
      return containerClass.isInstance(container) ? containerClass.cast(eventHandlerField.get(inv)) : null;
    } catch(final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static PlayerEntity findPlayerFromInv(final CraftingInventory inv) {
    try {
      final Container container = getContainer(inv, Container.class);

      if(container instanceof PlayerContainer) {
        return (PlayerEntity)containerPlayerPlayerField.get(container);
      }

      if(container instanceof WorkbenchContainer) {
        return (PlayerEntity)slotCraftingPlayerField.get(container.getSlot(0));
      }

      // Can't find player
      return null;
    } catch(final Throwable e) {
      GradientMod.logger.error("Failed to get player from inv", e);
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static <C extends IInventory, T extends IRecipe<C>> T findRecipe(final IRecipeType<T> type, final Predicate<T> match) {
    for(final IRecipe<C> recipe : GradientMod.getRecipeManager().getRecipes(type).values()) {
      if(match.test((T)recipe)) {
        return (T)recipe;
      }
    }

    return null;
  }
}
