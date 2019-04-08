package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.integrations.jei.crafting.CraftingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.crafting.ShapedRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.crafting.ShapelessRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.drying.DryingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.drying.DryingRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.firepit.FirePitRecipeCategory;
import lordmonoxide.gradient.integrations.jei.firepit.FirePitRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.fuel.FuelRecipeCategory;
import lordmonoxide.gradient.integrations.jei.fuel.FuelRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.grinding.GrindingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.grinding.GrindingRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.hardening.HardeningRecipeCategory;
import lordmonoxide.gradient.integrations.jei.hardening.HardeningRecipeWrapper;
import lordmonoxide.gradient.integrations.jei.mixing.MixingRecipeCategory;
import lordmonoxide.gradient.integrations.jei.mixing.MixingRecipeWrapper;
import lordmonoxide.gradient.inventory.ContainerPlayer3x3Crafting;
import lordmonoxide.gradient.recipes.AgeGatedShapedToolRecipe;
import lordmonoxide.gradient.recipes.AgeGatedShapelessToolRecipe;
import lordmonoxide.gradient.recipes.DryingRecipe;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import lordmonoxide.gradient.recipes.FuelRecipe;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.recipes.HardeningRecipe;
import lordmonoxide.gradient.recipes.MixingRecipe;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JEIPlugin
public class JeiIntegration implements IModPlugin {
  @Override
  public void registerCategories(final IRecipeCategoryRegistration registry) {
    final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new CraftingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new FirePitRecipeCategory(guiHelper));
    registry.addRecipeCategories(new MixingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GrindingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new HardeningRecipeCategory(guiHelper));
    registry.addRecipeCategories(new DryingRecipeCategory(guiHelper));
    registry.addRecipeCategories(new FuelRecipeCategory(guiHelper));
  }

  @Override
  public void register(final IModRegistry registry) {
    final IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

    for(final Item item : ForgeRegistries.ITEMS.getValuesCollection()) {
      if(item instanceof ItemTool || item instanceof ItemHoe || item instanceof ItemSword) {
        if("minecraft".equals(item.getRegistryName().getNamespace())) {
          blacklist.addIngredientToBlacklist(new ItemStack(item));
        }
      }
    }

    final IStackHelper stackHelper = registry.getJeiHelpers().getStackHelper();

    registry.handleRecipes(AgeGatedShapedToolRecipe.class, recipe -> new ShapedRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registry.handleRecipes(AgeGatedShapelessToolRecipe.class, recipe -> new ShapelessRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.CRAFTING);
    registry.handleRecipes(FirePitRecipe.class, recipe -> new FirePitRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FIREPIT);
    registry.handleRecipes(MixingRecipe.class, recipe -> new MixingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.MIXING);
    registry.handleRecipes(GrindingRecipe.class, recipe -> new GrindingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.GRINDING);
    registry.handleRecipes(HardeningRecipe.class, recipe -> new HardeningRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.HARDENING);
    registry.handleRecipes(DryingRecipe.class, recipe -> new DryingRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.DRYING);
    registry.handleRecipes(FuelRecipe.class, recipe -> new FuelRecipeWrapper(stackHelper, recipe), GradientRecipeCategoryUid.FUEL);
    registry.addRecipes(filterRecipes(AgeGatedShapedToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registry.addRecipes(filterRecipes(AgeGatedShapelessToolRecipe.class), GradientRecipeCategoryUid.CRAFTING);
    registry.addRecipes(filterRecipes(FirePitRecipe.class), GradientRecipeCategoryUid.FIREPIT);
    registry.addRecipes(filterRecipes(MixingRecipe.class), GradientRecipeCategoryUid.MIXING);
    registry.addRecipes(filterRecipes(GrindingRecipe.class), GradientRecipeCategoryUid.GRINDING);
    registry.addRecipes(filterRecipes(HardeningRecipe.class), GradientRecipeCategoryUid.HARDENING);
    registry.addRecipes(filterRecipes(DryingRecipe.class), GradientRecipeCategoryUid.DRYING);
    registry.addRecipes(filterRecipes(FuelRecipe.class), GradientRecipeCategoryUid.FUEL);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FIREPIT);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.MIXING_BASIN), GradientRecipeCategoryUid.MIXING);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.MANUAL_GRINDER), GradientRecipeCategoryUid.GRINDING);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.HARDENING);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.DRYING_RACK), GradientRecipeCategoryUid.DRYING);
    registry.addRecipeCatalyst(new ItemStack(GradientBlocks.FIRE_PIT), GradientRecipeCategoryUid.FUEL);

    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ContainerPlayer3x3CraftingTransferInfo(VanillaRecipeCategoryUid.CRAFTING));
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ContainerPlayer3x3CraftingTransferInfo(GradientRecipeCategoryUid.CRAFTING));
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

  private static final class ContainerPlayer3x3CraftingTransferInfo implements IRecipeTransferInfo<ContainerPlayer3x3Crafting> {
    private final String uid;

    private ContainerPlayer3x3CraftingTransferInfo(final String uid) {
      this.uid = uid;
    }

    @Override
    public Class<ContainerPlayer3x3Crafting> getContainerClass() {
      return ContainerPlayer3x3Crafting.class;
    }

    @Override
    public String getRecipeCategoryUid() {
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
