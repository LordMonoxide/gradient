package lordmonoxide.gradient.integrations.jei.crafting;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.integrations.jei.GradientRecipeCategoryUid;
import lordmonoxide.gradient.integrations.jei.JeiRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.List;

public class CraftingRecipeCategory extends JeiRecipeCategory<IRecipe> {
  private static final ResourceLocation ICON_LOCATION = GradientMod.resource("textures/gui/crafting_icon.png");
  private static final ResourceLocation BACKGROUND_LOCATION = new ResourceLocation("jei", "textures/gui/gui_vanilla.png");

  private static final int craftOutputSlot = 0;
  private static final int craftInputSlot1 = 1;

  private final ICraftingGridHelper craftingGridHelper;

  public CraftingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.CRAFTING, IRecipe.class, guiHelper.createDrawable(ICON_LOCATION, 0, 0, 16, 16), guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 60, 116, 54));
    this.craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1);
  }

  @Override
  public String getTitle() {
    return I18n.format("gui.jei.category.craftingTable");
  }

  @Override
  public void setIngredients(final IRecipe recipe, final IIngredients ingredients) {

  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final IRecipe recipe, final IIngredients ingredients) {
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

    if(recipe instanceof IShapedRecipe) {
      final IShapedRecipe shaped = (IShapedRecipe)recipe;
      this.craftingGridHelper.setInputs(guiItemStacks, inputs, shaped.getRecipeWidth(), shaped.getRecipeHeight());
    } else {
      this.craftingGridHelper.setInputs(guiItemStacks, inputs);
      recipeLayout.setShapeless();
    }

    guiItemStacks.set(craftOutputSlot, outputs.get(0));
  }
}
