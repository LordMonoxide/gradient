package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.function.Predicate;

public final class RecipeHelper {
  private RecipeHelper() { }

  private static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");
  private static final Field containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "player", "field_82862_h");
  private static final Field slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "player", "field_75238_b");

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
    } catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Age getPlayerAge(final EntityPlayer player) {
    final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress != null) {
      return progress.getAge();
    }

    return Age.AGE1;
  }

  public static boolean playerMeetsAgeRequirement(final EntityPlayer player, final Age age) {
    final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

    if(progress != null) {
      return progress.meetsAgeRequirement(age);
    }

    return true;
  }

  public static boolean playerMeetsAgeRequirement(final InventoryCrafting inv, final Age age) {
    final EntityPlayer player = RecipeHelper.findPlayerFromInv(inv);

    if(player != null) {
      return playerMeetsAgeRequirement(player, age);
    }

    return true;
  }

  @Nullable
  public static <T extends IRecipe> T findRecipe(final Class<T> recipeClass, final Predicate<T> match) {
    for(final IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
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
