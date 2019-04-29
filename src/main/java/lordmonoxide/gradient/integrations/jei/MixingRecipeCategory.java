package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.recipes.MixingRecipe;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class MixingRecipeCategory extends JeiRecipeCategory<MixingRecipe> {
  private static final ResourceLocation BACKGROUND_LOCATION = GradientMod.resource("textures/gui/recipe_mixing.png");

  public MixingRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.MIXING, MixingRecipe.class, guiHelper.createDrawableIngredient(new ItemStack(GradientItems.MIXING_BASIN)), guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format(GradientBlocks.MIXING_BASIN.getTranslationKey());
  }

  @Override
  public void setIngredients(final MixingRecipe recipe, final IIngredients ingredients) {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
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

  @Override
  public void draw(final MixingRecipe recipe, final double mouseX, final double mouseY) {
    final FontRenderer font = Minecraft.getInstance().fontRenderer;

    final String age = I18n.format("jei.age." + recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 4, 8, 0x404040);

    font.drawString(I18n.format("jei.mixer.passes", recipe.passes), 4, 46, 0x404040);
    font.drawString(I18n.format("jei.mixer.ticks", recipe.ticks), 4, 60, 0x404040);
  }
}
