package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.recipes.HardeningRecipe;
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

public class HardeningRecipeCategory extends JeiRecipeCategory<HardeningRecipe> {
  private static final ResourceLocation BACKGROUND_LOCATION = GradientMod.resource("textures/gui/recipe_hardening.png");

  public HardeningRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.HARDENING, HardeningRecipe.class, guiHelper.createDrawableIngredient(new ItemStack(GradientItems.FIRE_PIT)), guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 166, 72));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.hardening.name");
  }

  @Override
  public void setIngredients(final HardeningRecipe recipe, final IIngredients ingredients) {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
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

  @Override
  public void draw(final HardeningRecipe recipe, final double mouseX, final double mouseY) {
    final FontRenderer font = Minecraft.getInstance().fontRenderer;

    final String age = I18n.format("jei.age." + recipe.age.value());
    final String requirement = I18n.format("jei.requirement.age", age);
    font.drawString(requirement, 9, 8, 0x404040);

    font.drawString(I18n.format("jei.hardening.ticks", recipe.ticks), 9, 46, 0x404040);
    font.drawString(I18n.format("jei.hardening.instructions", recipe.ticks), 9, 60, 0x404040);
  }
}
