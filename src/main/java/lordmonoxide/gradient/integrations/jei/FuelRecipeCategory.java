package lordmonoxide.gradient.integrations.jei;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.recipes.FuelRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class FuelRecipeCategory extends JeiRecipeCategory<FuelRecipe> {
  private static final ResourceLocation BACKGROUND_LOCATION = GradientMod.resource("textures/gui/recipe_fuel.png");

  public FuelRecipeCategory(final IGuiHelper guiHelper) {
    super(GradientRecipeCategoryUid.FUEL, FuelRecipe.class, guiHelper.createDrawableIngredient(new ItemStack(Items.COAL)), guiHelper.createDrawable(BACKGROUND_LOCATION, 0, 0, 166, 68));
  }

  @Override
  public String getTitle() {
    return I18n.format("jei.fuel.name");
  }

  @Override
  public void setIngredients(final FuelRecipe recipe, final IIngredients ingredients) {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }

  @Override
  public void setRecipe(final IRecipeLayout recipeLayout, final FuelRecipe recipe, final IIngredients ingredients) {
    final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

    guiItemStacks.init(0, true, 4, 4);
    guiItemStacks.set(0, inputs.get(0));
  }

  @Override
  public void draw(final FuelRecipe recipe, final double mouseX, final double mouseY) {
    final FontRenderer font = Minecraft.getInstance().fontRenderer;

    font.drawString(I18n.format("jei.fuel.duration", recipe.duration), 5,  25, 0x404040);
    font.drawString(I18n.format("jei.fuel.ignitionTemp", recipe.ignitionTemp), 5, 36, 0x404040);
    font.drawString(I18n.format("jei.fuel.burnTemp", recipe.burnTemp), 5, 47, 0x404040);
    font.drawString(I18n.format("jei.fuel.heatPerSec", recipe.heatPerSec), 5, 58, 0x404040);
  }
}
