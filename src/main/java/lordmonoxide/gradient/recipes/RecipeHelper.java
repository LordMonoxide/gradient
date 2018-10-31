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
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public final class RecipeHelper {
  private RecipeHelper() { }

  private static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");
  private static final Field containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "player", "field_82862_h");
  private static final Field slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "player", "field_75238_b");

  public static Container getContainer(final InventoryCrafting inv) {
    try {
      return (Container)eventHandlerField.get(inv);
    } catch(final IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  public static EntityPlayer findPlayerFromInv(final InventoryCrafting inv) {
    try {
      final Container container = getContainer(inv);
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

    return Age.highest();
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
}
