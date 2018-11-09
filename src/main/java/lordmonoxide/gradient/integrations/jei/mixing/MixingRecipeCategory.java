package lordmonoxide.gradient.integrations.jei.mixing;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.mixingbasin.TileMixingBasin;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class MixingRecipeCategory extends JeiRecipeCategory<MixingRecipeWrapper> {
  @GameRegistry.ObjectHolder("gradient:mixing_basin")
  private static final Block MIXING_BASIN = null;

  public MixingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.MIXING, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_mixing.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return MIXING_BASIN.getLocalizedName();
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final MixingRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    for(int slot = 0; slot < TileMixingBasin.INPUT_SIZE; slot++) {
      guiItemStacks.init(slot, true, 8 + slot * 20, 25);
      guiItemStacks.set(slot, inputs.get(slot));
    }

    guiItemStacks.init(TileMixingBasin.INPUT_SIZE + 1, true, 86, 25);
    guiItemStacks.set(TileMixingBasin.INPUT_SIZE + 1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
