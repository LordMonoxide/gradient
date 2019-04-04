package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO: registerVanillaCategoryExtensions
//TODO: registerItemSubtypes
// https://github.com/mezz/JustEnoughItems/blob/1.13/src/main/java/mezz/jei/plugins/vanilla/VanillaPlugin.java

@JeiPlugin
public class JeiIntegration implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return GradientMod.resource("gradient");
  }

  @Override
  public void registerCategories(final IRecipeCategoryRegistration registry) {
/*
    final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new CraftingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new FirePitRecipeCategory(guiHelper));
    registry.addRecipeCategories(new MixingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GrindingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new HardeningRecipeCategory(guiHelper));
    registry.addRecipeCategories(new DryingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new FuelRecipeCategory(guiHelper));
*/
  }

  @Override
  public void registerRecipes(final IRecipeRegistration registration) {
    //TODO: blacklist
/*
    final IIngredientBlacklist blacklist = registration.getJeiHelpers().getIngredientBlacklist();

    for(final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword) {
        if("minecraft".equals(item.getRegistryName().getNamespace())) {
          blacklist.addIngredientToBlacklist(new ItemStack(item));
        }
      }
    }
*/

    final IStackHelper stackHelper = registration.getJeiHelpers().getStackHelper();

    //TODO
/*
    registration.handleRecipes(AgeGatedShapedToolRecipe.class, recipe -> new ShapedRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registration.handleRecipes(AgeGatedShapelessToolRecipe.class, recipe -> new ShapelessRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registration.handleRecipes(FirePitRecipe.class, recipe -> new FirePitRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FIREPIT);
    registration.handleRecipes(MixingRecipe.class, recipe -> new MixingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.MIXING);
    registration.handleRecipes(GrindingRecipe.class, recipe -> new GrindingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.GRINDING);
    registration.handleRecipes(HardeningRecipe.class, recipe -> new HardeningRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.HARDENING);
    registration.handleRecipes(DryingRecipe.class, recipe -> new DryingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.DRYING);
    registration.handleRecipes(FuelRecipe.class, recipe -> new FuelRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FUEL);
*/
/*
    registration.addRecipes(filterRecipes(AgeGatedShapedToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registration.addRecipes(filterRecipes(AgeGatedShapelessToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registration.addRecipes(filterRecipes(FirePitRecipe.class), GradientRecipeCategoryUid.FIREPIT);
    registration.addRecipes(filterRecipes(MixingRecipe.class), GradientRecipeCategoryUid.MIXING);
    registration.addRecipes(filterRecipes(GrindingRecipe.class), GradientRecipeCategoryUid.GRINDING);
    registration.addRecipes(filterRecipes(HardeningRecipe.class), GradientRecipeCategoryUid.HARDENING);
    registration.addRecipes(filterRecipes(DryingRecipe.class), GradientRecipeCategoryUid.DRYING);
    registration.addRecipes(filterRecipes(FuelRecipe.class), GradientRecipeCategoryUid.FUEL);
*/
  }

  @Override
  public void registerRecipeCatalysts(final IRecipeCatalystRegistration registration) {
/*
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FIREPIT);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.MIXING_BASIN), GradientRecipeCategoryUid.MIXING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.MANUAL_GRINDER), GradientRecipeCategoryUid.GRINDING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.HARDENING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.DRYING_RACK), GradientRecipeCategoryUid.DRYING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FUEL);
*/
  }

  private static <T extends IRecipe> Collection<T> filterRecipes(final Class<T> recipeClass) {
    final List<T> recipes = new ArrayList<>();

    for(final IRecipe recipe : GradientMod.getRecipeManager().getRecipes()) {
      if(recipe.getClass().isAssignableFrom(recipeClass)) {
        recipes.add(recipeClass.cast(recipe));
      }
    }

    return recipes;
  }
}
