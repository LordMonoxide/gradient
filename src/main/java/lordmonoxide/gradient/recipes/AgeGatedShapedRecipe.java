package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class AgeGatedShapedRecipe extends ShapedRecipes {
  private final Age age;

  public AgeGatedShapedRecipe(final String group, final Age age, final int width, final int height, final NonNullList<Ingredient> ingredients, final ItemStack result) {
    super(group, width, height, ingredients, result);
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
