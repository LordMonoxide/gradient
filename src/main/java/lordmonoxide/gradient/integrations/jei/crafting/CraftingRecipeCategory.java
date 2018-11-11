package lordmonoxide.gradient.integrations.jei.crafting;

import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CraftingRecipeCategory extends JeiRecipeCategory<RecipeWrapper> {
  private static final int craftOutputSlot = 0;
  private static final int craftInputSlot1 = 1;

  private final ICraftingGridHelper craftingGridHelper;

  public CraftingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.CRAFTING, guiHelper.createDrawable(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 60, 116, 54));
    this.craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
  }

  @Override
  public String getTitle() {
    return I18n.format("gui.jei.category.craftingTable");
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final RecipeWrapper recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

    guiItemStacks.init(craftOutputSlot, false, 94, 18);

    for(int y = 0; y < 3; ++y) {
      for(int x = 0; x < 3; ++x) {
        final int index = craftInputSlot1 + x + y * 3;
        guiItemStacks.init(index, true, x * 18, y * 18);
      }
    }

    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
    final List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

    if(recipe instanceof IShapedCraftingRecipeWrapper) {
      final IShapedCraftingRecipeWrapper shaped = (IShapedCraftingRecipeWrapper)recipe;
      this.craftingGridHelper.setInputs(guiItemStacks, inputs, shaped.getWidth(), shaped.getHeight());
    } else {
      this.craftingGridHelper.setInputs(guiItemStacks, inputs);
      recipeLayout.setShapeless();
    }

    guiItemStacks.set(craftOutputSlot, outputs.get(0));
  }
}
