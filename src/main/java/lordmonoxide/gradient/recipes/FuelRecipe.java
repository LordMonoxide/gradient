package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class FuelRecipe implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final ResourceLocation id;
  private final String group;
  public final int   duration;
  public final float ignitionTemp;
  public final float burnTemp;
  public final float heatPerSec;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public FuelRecipe(final ResourceLocation id, final String group, final int duration, final float ignitionTemp, final float burnTemp, final float heatPerSec, final Ingredient input) {
    this.id = id;
    this.group = group;
    this.duration = duration;
    this.ignitionTemp = ignitionTemp;
    this.burnTemp = burnTemp;
    this.heatPerSec = heatPerSec;
    this.input = NonNullList.from(Ingredient.EMPTY, input);
    this.isSimple = input.isSimple();
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  @Override
  public ResourceLocation getId() {
    return this.id;
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return GradientRecipeSerializers.FUEL;
  }

  @Override
  public IRecipeType<? extends IRecipe> getType() {
    return GradientRecipeTypes.FUEL;
  }

  @Override
  @Deprecated
  public boolean matches(final IInventory inv, final World world) {
    return false;
  }

  public boolean matches(final ItemStack stack) {
    recipeItemHelper.clear();
    inputStacks.clear();

    if(this.isSimple) {
      recipeItemHelper.accountStack(stack, 1);
      return recipeItemHelper.canCraft(this, null);
    }

    inputStacks.add(stack);
    return RecipeMatcher.findMatches(inputStacks, this.input) != null;
  }

  @Override
  @Deprecated
  public ItemStack getCraftingResult(final IInventory inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.input.size();
  }

  @Override
  public ItemStack getRecipeOutput() {
    //TODO: this is here temporarily to pass JEI's recipe validator
    return this.input.get(0).getMatchingStacks()[0];
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.input;
  }

  public static final class Serializer implements IRecipeSerializer<FuelRecipe> {
    @Override
    public FuelRecipe read(final ResourceLocation recipeId, final JsonObject json) {
      final String group = JSONUtils.getString(json, "group", "");
      final int   duration = JSONUtils.getInt(json, "duration");
      final float ignitionTemp = JSONUtils.getFloat(json, "ignitionTemp");
      final float burnTemp = JSONUtils.getFloat(json, "burnTemp");
      final float heatPerSec = JSONUtils.getFloat(json, "heatPerSec");
      final Ingredient ingredient = CraftingHelper.getIngredient(JSONUtils.getJsonObject(json, "ingredient"));

      return new FuelRecipe(recipeId, group, duration, ignitionTemp, burnTemp, heatPerSec, ingredient);
    }

    @Override
    public FuelRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      final String group = buffer.readString(32767);
      final int duration = buffer.readVarInt();
      final float ignitionTemp = buffer.readFloat();
      final float burnTemp = buffer.readFloat();
      final float heatPerSec = buffer.readFloat();
      final Ingredient ingredient = Ingredient.read(buffer);

      return new FuelRecipe(recipeId, group, duration, ignitionTemp, burnTemp, heatPerSec, ingredient);
    }

    @Override
    public void write(final PacketBuffer buffer, final FuelRecipe recipe) {
      buffer.writeString(recipe.group);
      buffer.writeVarInt(recipe.duration);
      buffer.writeFloat(recipe.ignitionTemp);
      buffer.writeFloat(recipe.burnTemp);
      buffer.writeFloat(recipe.heatPerSec);
      recipe.input.get(0).write(buffer);
    }

    @Override
    public ResourceLocation getName() {
      return GradientRecipeTypes.FUEL.getId();
    }
  }
}
