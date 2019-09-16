package lordmonoxide.gradient.recipes;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import lordmonoxide.gradient.items.GradientItemTool;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;
import java.util.Random;

public class AgeGatedShapelessToolRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static final Random rand = new Random();

  private final ShapelessRecipes recipe;
  public final Age age;

  public AgeGatedShapelessToolRecipe(final String group, final Age age, final ItemStack output, final NonNullList<Ingredient> ingredients) {
    this.recipe = new ShapelessRecipes(group, output, ingredients);
    this.age = age;
  }

  @Override
  public boolean matches(final InventoryCrafting inv, final World world) {
    final boolean matches = AgeUtils.playerMeetsAgeRequirement(inv, this.age) && this.recipe.matches(inv, world);

    if(Loader.isModLoaded("ic2")) {
      return matches && matchesIc2(inv);
    }

    return matches;
  }

  @Override
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.recipe.getCraftingResult(inv);
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return this.recipe.canFit(width, height);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.recipe.getRecipeOutput();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
    final NonNullList<ItemStack> remaining = IRecipe.super.getRemainingItems(inv);

    for(int slot = 0; slot < remaining.size(); ++slot) {
      final ItemStack stack = inv.getStackInSlot(slot);

      if(stack.getItem() instanceof GradientItemTool) {
        stack.attemptDamageItem(1, rand, null);

        if(stack.isItemStackDamageable() && stack.getItemDamage() > stack.getMaxDamage()) {
          ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), stack, null);
          remaining.set(slot, ItemStack.EMPTY);
        } else {
          remaining.set(slot, stack.copy());
        }
      }

      if(Loader.isModLoaded("ic2")) {
        processIc2Tool(remaining, slot, stack);
      }
    }

    return remaining;
  }

  private static final int EU_USAGE = 100;

  @Optional.Method(modid = "ic2")
  public static boolean matchesIc2(final InventoryCrafting inv) {
    final IElectricItemManager manager = ElectricItem.manager;

    for(int x = 0; x < inv.getWidth(); x++) {
      for(int y = 0; y < inv.getHeight(); y++) {
        final ItemStack stack = inv.getStackInRowAndColumn(x, y);

        if(stack.getItem() instanceof IElectricItem) {
          if(manager.getCharge(stack) < EU_USAGE) {
            return false;
          }
        }
      }
    }

    return true;
  }

  @Optional.Method(modid = "ic2")
  public static boolean processIc2Tool(final List<ItemStack> remaining, final int slot, final ItemStack stack) {
    final IElectricItemManager manager = ElectricItem.manager;

    if(stack.getItem() instanceof IElectricItem) {
      if(manager.getCharge(stack) >= EU_USAGE) {
        manager.discharge(stack, EU_USAGE, 1, false, false, false);
        remaining.set(slot, stack.copy());
        return true;
      }
    }

    return false;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.recipe.getIngredients();
  }

  @Override
  public boolean isDynamic() {
    return this.recipe.isDynamic();
  }

  @Override
  public String getGroup() {
    return this.recipe.getGroup();
  }
}
