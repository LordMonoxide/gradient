package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.progress.Age;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

import java.util.ArrayList;
import java.util.List;

public class HardeningRecipe implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final ResourceLocation id;
  private final String group;
  public final Age age;
  public final int ticks;
  private final ItemStack output;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public HardeningRecipe(final ResourceLocation id, final String group, final Age age, final int ticks, final ItemStack output, final Ingredient input) {
    this.id = id;
    this.group = group;
    this.age = age;
    this.ticks = ticks;
    this.output = output;
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
    return GradientRecipeSerializers.HARDENING;
  }

  @Override
  @Deprecated
  public boolean matches(final IInventory inv, final World world) {
    return false;
  }

  public boolean matches(final IBlockState state, final Age age) {
    if(age.ordinal() < this.age.ordinal()) {
      return false;
    }

    final Block block = state.getBlock();
    final ItemStack stack = new ItemStack(block);

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
    return this.output.copy();
  }

  public IBlockState getCraftingResult() {
    return Block.getBlockFromItem(this.output.getItem()).getDefaultState();
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

  public static final class Serializer implements IRecipeSerializer<HardeningRecipe> {
    @Override
    public HardeningRecipe read(final ResourceLocation recipeId, final JsonObject json) {
      final String group = JsonUtils.getString(json, "group", "");
      final Age age = Age.get(JsonUtils.getInt(json, "age", 1));
      final int ticks = JsonUtils.getInt(json, "ticks");

      final Ingredient ingredient = CraftingHelper.getIngredient(JsonUtils.getJsonObject(json, "ingredient"));
      final ItemStack output = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), true);

      return new HardeningRecipe(recipeId, group, age, ticks, output, ingredient);
    }

    @Override
    public HardeningRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      final String group = buffer.readString(32767);
      final Age age = Age.get(buffer.readVarInt());
      final int ticks = buffer.readVarInt();

      final Ingredient ingredient = Ingredient.read(buffer);
      final ItemStack output = buffer.readItemStack();

      return new HardeningRecipe(recipeId, group, age, ticks, output, ingredient);
    }

    @Override
    public void write(final PacketBuffer buffer, final HardeningRecipe recipe) {
      buffer.writeString(recipe.group);
      buffer.writeVarInt(recipe.age.value());
      buffer.writeVarInt(recipe.ticks);
      recipe.input.get(0).write(buffer);
      buffer.writeItemStack(recipe.output);
    }

    @Override
    public ResourceLocation getName() {
      return GradientRecipeTypes.HARDENING.getId();
    }
  }
}
