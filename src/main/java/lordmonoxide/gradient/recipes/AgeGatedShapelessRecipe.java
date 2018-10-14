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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class AgeGatedShapelessRecipe extends ShapelessRecipes {
  private final Age age;

  public AgeGatedShapelessRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    super(group, output, ingredients);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    final EntityPlayer player = findPlayer(inv);

    if(player != null) {
      final PlayerProgress progress = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

      if(progress != null) {
        if(!progress.meetsAgeRequirement(this.age)) {
          return false;
        }
      }
    }

    return super.matches(inv, world);
  }

  // TODO: SRG names for non-dev environment
  private static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler");
  private static final Field containerPlayerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "player");
  private static final Field slotCraftingPlayerField = ReflectionHelper.findField(SlotCrafting.class, "player");

  @Nullable
  private static EntityPlayer findPlayer(final InventoryCrafting inv) {
    try {
      final Container container = (Container)eventHandlerField.get(inv);
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
}
