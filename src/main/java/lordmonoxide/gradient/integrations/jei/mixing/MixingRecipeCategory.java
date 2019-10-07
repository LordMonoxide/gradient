package lordmonoxide.gradient.integrations.jei.mixing;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MixingRecipeCategory extends JeiRecipeCategory<MixingRecipeWrapper> {
  public MixingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.MIXING, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_mixing.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return GradientBlocks.MIXING_BASIN.getLocalizedName();
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final MixingRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    for(int slot = 0; slot < Math.min(TileMixingBasin.INPUT_SIZE, inputs.size()); slot++) {
      guiItemStacks.init(slot, true, 3 + slot * 20, 25);
      guiItemStacks.set(slot, inputs.get(slot));
    }

    guiItemStacks.init(TileMixingBasin.INPUT_SIZE + 1, true, 143, 25);
    guiItemStacks.set(TileMixingBasin.INPUT_SIZE + 1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

    guiFluidStacks.init(0, true, 106, 26);
    guiFluidStacks.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));

    guiFluidStacks.addTooltipCallback((slot, input, fluid, tooltip) -> tooltip.add(I18n.format("jei.mixer.amount", fluid.amount)));
  }
}
