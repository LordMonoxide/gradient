package lordmonoxide.gradient.integrations.jei.firepit;

import lordmonoxide.gradient.GradientMod;
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

public class FirePitRecipeCategory extends JeiRecipeCategory<FirePitRecipeWrapper> {
  @GameRegistry.ObjectHolder("gradient:fire_pit")
  private static final Block FIRE_PIT = null;

  public FirePitRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.FIREPIT, guiHelper.createDrawable(GradientMod.resource("textures/gui/recipe_grinding.png"), 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return FIRE_PIT.getLocalizedName();
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final FirePitRecipeWrapper recipeWrapper, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 8, 25);
    guiItemStacks.set(0, inputs.get(0));

    guiItemStacks.init(1, true, 46, 25);
    guiItemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }
}
