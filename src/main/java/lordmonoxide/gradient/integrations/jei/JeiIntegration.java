package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
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
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.GRINDSTONE), GradientRecipeCategoryUid.GRINDING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.HARDENING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.DRYING_RACK), GradientRecipeCategoryUid.DRYING);
    registration.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FUEL);
  }

  @Override
  public void registerRecipeTransferHandlers(final IRecipeTransferRegistration registration) {
    registration.addRecipeTransferHandler(new ContainerPlayer3x3CraftingTransferInfo(VanillaRecipeCategoryUid.CRAFTING));
  }

  @Override
  public void registerVanillaCategoryExtensions(final IVanillaCategoryExtensionRegistration registration) {
    registration.getCraftingCategory().addCategoryExtension(AgeGatedShapedToolRecipe.class, ShapedAgeCraftingExtension::new);
    registration.getCraftingCategory().addCategoryExtension(AgeGatedShapelessToolRecipe.class, ShapelessAgeCraftingExtension::new);
  }

  private static <T extends IRecipe> Collection<T> filterRecipes(final Class<T> recipeClass) {
    return GradientMod.getRecipeManager().getRecipes().stream()
      .filter(recipe -> recipe.getClass().isAssignableFrom(recipeClass))
      .map(recipeClass::cast)
      .collect(Collectors.toList());
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
