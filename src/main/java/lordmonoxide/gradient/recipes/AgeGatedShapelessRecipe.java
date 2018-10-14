package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AgeGatedShapelessRecipe extends ShapelessRecipes {
  private final Age age;

  public AgeGatedShapelessRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    super(group, output, ingredients);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    final EntityPlayer player = RecipeHelper.findPlayerFromInv(inv);

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
}
