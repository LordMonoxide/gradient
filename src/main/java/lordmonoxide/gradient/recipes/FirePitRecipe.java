package lordmonoxide.gradient.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class FirePitRecipe implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final ResourceLocation id;
  private final String group;
  public final Age age;
  public final int ticks;
  public final float temperature;
  private final ItemStack output;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public FirePitRecipe(final ResourceLocation id, final String group, final Age age, final int ticks, final float temperature, final ItemStack output, final NonNullList<Ingredient> input) {
    this.id = id;
    this.group = group;
    this.age = age;
    this.ticks = ticks;
    this.temperature = temperature;
    this.output = output;
    this.input = input;

    boolean isSimple = true;
    for(final Ingredient ingredient : input) {
      isSimple &= ingredient.isSimple();
    }

    this.isSimple = isSimple;
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
    return GradientRecipeSerializers.FIREPIT;
  }

  @Override
  @Deprecated
  public boolean matches(final IInventory inv, final World world) {
    return false;
  }

  public boolean matches(final IItemHandler inv, final Age age, final int firstSlot, final int lastSlot) {
    if(age.ordinal() < this.age.ordinal()) {
      return false;
    }

    recipeItemHelper.clear();
    inputStacks.clear();

    int ingredientCount = 0;
    for(int slot = firstSlot; slot <= lastSlot; ++slot) {
      final ItemStack itemstack = inv.getStackInSlot(slot);

      if(!itemstack.isEmpty()) {
        ++ingredientCount;

        if(this.isSimple) {
          recipeItemHelper.accountStack(itemstack, 1);
        } else {
          inputStacks.add(itemstack);
        }
      }
    }

    if(ingredientCount != this.input.size()) {
      return false;
    }

    if(this.isSimple) {
      return recipeItemHelper.canCraft(this, null);
    }

    return RecipeMatcher.findMatches(inputStacks, this.input) != null;
  }

  @Override
  public ItemStack getCraftingResult(final IInventory inv) {
    return this.output.copy();
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return width * height >= this.input.size();
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.output;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.input;
  }

  public static final class Serializer implements IRecipeSerializer<FirePitRecipe> {
    @Override
    public FirePitRecipe read(final ResourceLocation recipeId, final JsonObject json) {
      final String group = JsonUtils.getString(json, "group", "");
      final Age age = Age.get(JsonUtils.getInt(json, "age", 1));
      final int ticks = JsonUtils.getInt(json, "ticks");
      final float temperature = JsonUtils.getFloat(json, "temperature");

      final NonNullList<Ingredient> ingredients = NonNullList.create();
      for(final JsonElement element : JsonUtils.getJsonArray(json, "ingredients")) {
        ingredients.add(CraftingHelper.getIngredient(element));
      }

      if(ingredients.isEmpty()) {
        throw new JsonParseException("No ingredients for firepit recipe");
      }

      final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), true);

      return new FirePitRecipe(recipeId, group, age, ticks, temperature, output, ingredients);
    }

    @Override
    public FirePitRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      final String group = buffer.readString(32767);
      final Age age = Age.get(buffer.readVarInt());
      final int ticks = buffer.readVarInt();
      final float temperature = buffer.readFloat();

      final int inputSize = buffer.readVarInt();
      final NonNullList<Ingredient> inputs = NonNullList.withSize(inputSize, Ingredient.EMPTY);

      for(int input = 0; input < inputs.size(); ++input) {
        inputs.set(input, Ingredient.read(buffer));
      }

      final ItemStack output = buffer.readItemStack();

      return new FirePitRecipe(recipeId, group, age, ticks, temperature, output, inputs);
    }

    @Override
    public void write(final PacketBuffer buffer, final FirePitRecipe recipe) {
      buffer.writeString(recipe.group);
      buffer.writeVarInt(recipe.age.value());
      buffer.writeVarInt(recipe.ticks);
      buffer.writeFloat(recipe.temperature);
      buffer.writeVarInt(recipe.input.size());

      for(final Ingredient ingredient : recipe.input) {
        ingredient.write(buffer);
      }

      buffer.writeItemStack(recipe.output);
    }

    @Override
    public ResourceLocation getName() {
      return GradientRecipeTypes.FIREPIT.getId();
    }
  }
}
