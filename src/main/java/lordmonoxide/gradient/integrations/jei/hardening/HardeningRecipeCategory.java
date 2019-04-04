package lordmonoxide.gradient.integrations.jei.hardening;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import lordmonoxide.gradient.recipes.HardeningRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.List;

public class HardeningRecipeCategory extends JeiRecipeCategory<HardeningRecipe> {
  public HardeningRecipeCategory(final IGuiHelper guiHelper) {
    //TODO
    super(GradientRecipeCategoryUid.HARDENING, HardeningRecipe.class, null, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_hardening.png"), 0, 0, 166, 72));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.hardening.name");
  }

  @Override
  public void setIngredients(final HardeningRecipe recipe, final IIngredients ingredients) {

  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final HardeningRecipe recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 8, 25);
    guiItemStacks.set(0, inputs.get(0));

    guiItemStacks.init(1, true, 46, 25);
    guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
