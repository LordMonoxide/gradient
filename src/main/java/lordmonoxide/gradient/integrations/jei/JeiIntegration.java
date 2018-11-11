package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.integrations.jei.crafting.CraftingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.crafting.ShapedRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.firepit.FirePitRecipeCategory;
import lordmonoxide.gradient.integrations.jei.firepit.FirePitRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.grinding.GrindingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.grinding.GrindingRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.mixing.MixingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.mixing.MixingRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.crafting.ShapelessRecipeWrapper;
import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import lordmonoxide.gradient.recipes.AgeGatedShapelessToolRecipe;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.recipes.MixingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JEIPlugin
public class JeiIntegration implements IModPlugin {
  @GameRegistry.ObjectHolder("gradient:fire_pit")
  private static final Block FIRE_PIT = null;

  @GameRegistry.ObjectHolder("gradient:mixing_basin")
  private static final Block MIXING_BASIN = null;

  @GameRegistry.ObjectHolder("gradient:manual_grinder")
  private static final Block MANUAL_GRINDER = null;

  @GameRegistry.ObjectHolder("gradient:firepit_discriminator")
  private static final Item FIREPIT_DISCRIMINATOR = null;

  @GameRegistry.ObjectHolder("gradient:mixing_discriminator")
  private static final Item MIXING_DISCRIMINATOR = null;

  @GameRegistry.ObjectHolder("gradient:grinding_discriminator")
  private static final Item GRINDING_DISCRIMINATOR = null;

  @Override
  public void registerCategories(final IRecipeCategoryRegistration registry) {
    final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new CraftingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new FirePitRecipeCategory(guiHelper));
    registry.addRecipeCategories(new MixingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GrindingRecipeCategory(guiHelper));
  }

  @Override
  public void register(final IModRegistry registry) {
    final IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
    blacklist.addIngredientToBlacklist(new ItemStack(FIREPIT_DISCRIMINATOR));
    blacklist.addIngredientToBlacklist(new ItemStack(MIXING_DISCRIMINATOR));
    blacklist.addIngredientToBlacklist(new ItemStack(GRINDING_DISCRIMINATOR));

    for(final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      if(item instanceof ItemTool && "minecraft".equals(item.getRegistryName().getNamespace())) {
        blacklist.addIngredientToBlacklist(new ItemStack(item));
      }
    }

    final IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();

    registry.handleRecipes(AgeGatedShapedToolRecipe.class, recipe -> new ShapedRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registry.handleRecipes(AgeGatedShapelessToolRecipe.class, recipe -> new ShapelessRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registry.handleRecipes(FirePitRecipe.class, recipe -> new FirePitRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FIREPIT);
    registry.handleRecipes(MixingRecipe.class, recipe -> new MixingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.MIXING);
    registry.handleRecipes(GrindingRecipe.class, recipe -> new GrindingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.GRINDING);
    registry.addRecipes(filterRecipes(AgeGatedShapedToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registry.addRecipes(filterRecipes(AgeGatedShapelessToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registry.addRecipes(filterRecipes(FirePitRecipe.class), GradientRecipeCategoryUid.FIREPIT);
    registry.addRecipes(filterRecipes(MixingRecipe.class), GradientRecipeCategoryUid.MIXING);
    registry.addRecipes(filterRecipes(GrindingRecipe.class), GradientRecipeCategoryUid.GRINDING);
    registry.addRecipeCatalyst(new ItemStack(FIRE_PIT), GradientRecipeCategoryUid.FIREPIT);
    registry.addRecipeCatalyst(new ItemStack(MIXING_BASIN), GradientRecipeCategoryUid.MIXING);
    registry.addRecipeCatalyst(new ItemStack(MANUAL_GRINDER), GradientRecipeCategoryUid.GRINDING);
  }

  private static <T extends IRecipe> Collection<T> filterRecipes(final Class<T> recipeClass) {
    final List<T> recipes = new ArrayList<>();

    for(final IRecipe recipe : ForgeRegistries.RECIPES.getValuesCollection()) {
      if(recipe.getClass().isAssignableFrom(recipeClass)) {
        recipes.add(recipeClass.cast(recipe));
      }
    }

    return recipes;
  }
}
