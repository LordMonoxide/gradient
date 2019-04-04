package lordmonoxide.gradient.integrations.jei.mixing;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import lordmonoxide.gradient.recipes.MixingRecipe;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MixingRecipeCategory extends JeiRecipeCategory<MixingRecipe> {
  public MixingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.MIXING, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_mixing.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return GradientBlocks.MIXING_BASIN.getLocalizedName();
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final MixingRecipe recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    for(int slot = 0; slot < Math.min(TileMixingBasin.INPUT_SIZE, inputs.size()); slot++) {
      guiItemStacks.init(slot, true, 3 + slot * 20, 25);
      guiItemStacks.set(slot, inputs.get(slot));
    }

    guiItemStacks.init(TileMixingBasin.INPUT_SIZE + 1, true, 121, 25);
    guiItemStacks.set(TileMixingBasin.INPUT_SIZE + 1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
