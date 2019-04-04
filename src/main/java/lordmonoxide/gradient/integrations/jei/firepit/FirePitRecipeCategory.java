package lordmonoxide.gradient.integrations.jei.firepit;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import lordmonoxide.gradient.recipes.FirePitRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FirePitRecipeCategory extends JeiRecipeCategory<FirePitRecipe> {
  public FirePitRecipeCategory(final IGuiHelper guiHelper) {
    //TODO
    super(GradientRecipeCategoryUid.FIREPIT, FirePitRecipe.class, null, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_grinding.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey());
  }

  @Override
  public void setIngredients(final FirePitRecipe recipe, final IIngredients ingredients) {

  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final FirePitRecipe recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 8, 25);
    guiItemStacks.set(0, inputs.get(0));

    guiItemStacks.init(1, true, 46, 25);
    guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
