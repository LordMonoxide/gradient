package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.recipes.FirePitRecipe;
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

public class FirePitRecipeCategory extends JeiRecipeCategory<FirePitRecipe> {
  private static final ResourceLocation BACKGROUND_LOCATION = GradientMod.resource("textures/gui/recipe_grinding.png");

  public FirePitRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.FIREPIT, FirePitRecipe.class, guiHelper.createDrawableIngredient(new ItemStack(GradientItems.FIRE_PIT)), guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey());
  }

  @Override
  public void setIngredients(final FirePitRecipe recipe, final IIngredients ingredients) {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
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

  @Override
  public void draw(final FirePitRecipe recipe, final double mouseX, final double mouseY) {
    final FontRenderer font = Minecraft.getInstance().fontRenderer;

    final String age = I18n.format("jei.age." + recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 9, 8, 0x404040);

    font.drawString(I18n.format("jei.firepit.temperature", recipe.temperature), 9, 46, 0x404040);
    font.drawString(I18n.format("jei.firepit.ticks", recipe.ticks), 9, 60, 0x404040);
  }
}
