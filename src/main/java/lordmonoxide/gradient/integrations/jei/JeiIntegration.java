package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.integrations.jei.crafting.CraftingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.drying.DryingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.firepit.FirePitRecipeCategory;
import lordmonoxide.gradient.integrations.jei.fuel.FuelRecipeCategory;
import lordmonoxide.gradient.integrations.jei.grinding.GrindingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.hardening.HardeningRecipeCategory;
import lordmonoxide.gradient.integrations.jei.mixing.MixingRecipeCategory;
import lordmonoxide.gradient.inventory.ContainerPlayer3x3Crafting;
import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import lordmonoxide.gradient.recipes.AgeGatedShapelessToolRecipe;
import lordmonoxide.gradient.recipes.DryingRecipe;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.recipes.FuelRecipe;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.recipes.HardeningRecipe;
import lordmonoxide.gradient.recipes.MixingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
  public void registerCategories(final IRecipeCategoryRegistration registration) {
    final IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
    registration.addRecipeCategories(new CraftingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new FirePitRecipeCategory(guiHelper));
    registration.addRecipeCategories(new MixingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new GrindingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new HardeningRecipeCategory(guiHelper));
    registration.addRecipeCategories(new DryingRecipeCategory(guiHelper));
    registration.addRecipeCategories(new FuelRecipeCategory(guiHelper));
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
    registration.handleRecipes(FirePitRecipe.class, recipe -> new FirePitRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FIREPIT);
    registration.handleRecipes(MixingRecipe.class, recipe -> new MixingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.MIXING);
    registration.handleRecipes(GrindingRecipe.class, recipe -> new GrindingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.GRINDING);
    registration.handleRecipes(HardeningRecipe.class, recipe -> new HardeningRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.HARDENING);
    registration.handleRecipes(DryingRecipe.class, recipe -> new DryingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.DRYING);
    registration.handleRecipes(FuelRecipe.class, recipe -> new FuelRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FUEL);
*/
    registration.addRecipes(filterRecipes(AgeGatedShapedToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registration.addRecipes(filterRecipes(AgeGatedShapelessToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registration.addRecipes(filterRecipes(FirePitRecipe.class), GradientRecipeCategoryUid.FIREPIT);
    registration.addRecipes(filterRecipes(MixingRecipe.class), GradientRecipeCategoryUid.MIXING);
    registration.addRecipes(filterRecipes(GrindingRecipe.class), GradientRecipeCategoryUid.GRINDING);
    registration.addRecipes(filterRecipes(HardeningRecipe.class), GradientRecipeCategoryUid.HARDENING);
    registration.addRecipes(filterRecipes(DryingRecipe.class), GradientRecipeCategoryUid.DRYING);
    registration.addRecipes(filterRecipes(FuelRecipe.class), GradientRecipeCategoryUid.FUEL);
  }

  @Override
  public void registerRecipeCatalysts(final IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FIREPIT);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.MIXING_BASIN), GradientRecipeCategoryUid.MIXING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.MANUAL_GRINDER), GradientRecipeCategoryUid.GRINDING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.HARDENING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.DRYING_RACK), GradientRecipeCategoryUid.DRYING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FUEL);
  }

  @Override
  public void registerRecipeTransferHandlers(final IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(new ContainerPlayer3x3CraftingTransferInfo(VanillaRecipeCategoryUid.CRAFTING));
    registration.addRecipeTransferHandler(new ContainerPlayer3x3CraftingTransferInfo(GradientRecipeCategoryUid.CRAFTING));
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

  private static final class ContainerPlayer3x3CraftingTransferInfo implements IRecipeTransferInfo<ContainerPlayer3x3Crafting> {
    private final ResourceLocation uid;

    private ContainerPlayer3x3CraftingTransferInfo(final ResourceLocation uid) {
      this.uid = uid;
    }

    @Override
    public Class<ContainerPlayer3x3Crafting> getContainerClass() {
      return ContainerPlayer3x3Crafting.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
      return this.uid;
    }

    @Override
    public boolean canHandle(final ContainerPlayer3x3Crafting container) {
      return true;
    }

    @Override
    public List<Slot> getRecipeSlots(final ContainerPlayer3x3Crafting container) {
      return container.craftingSlots;
    }

    @Override
    public List<Slot> getInventorySlots(final ContainerPlayer3x3Crafting container) {
      return container.invSlots;
    }
  }
}
