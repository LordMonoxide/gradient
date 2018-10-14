package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.items.GradientItemTool;
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
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public class AgeGatedShapelessToolRecipe extends ShapelessRecipes {
  private static final Random rand = new Random();

  private final Age age;

  public AgeGatedShapelessToolRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
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

  @Override
  public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
    final NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for(int i = 0; i < list.size(); ++i) {
      final ItemStack stack = inv.getStackInSlot(i);

      if(stack.getItem() instanceof GradientItemTool) {
        stack.attemptDamageItem(1, rand, null);

        if(stack.isItemStackDamageable() && stack.getItemDamage() > stack.getMaxDamage()) {
          list.set(i, ItemStack.EMPTY);
        } else {
          list.set(i, stack.copy());
        }
      } else {
        list.set(i, ForgeHooks.getContainerItem(stack));
      }
    }

    return list;
  }
}
