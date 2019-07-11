package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.IShapedRecipe;

import java.util.Random;

public class AgeGatedShapedToolRecipe implements IShapedRecipe, GradientRecipe {
  private static final Random rand = new Random();

  private final ShapedRecipe recipe;
  public final Age age;

  public AgeGatedShapedToolRecipe(final ShapedRecipe recipe, final Age age) {
    this.recipe = recipe;
    this.age = age;
  }

  @Override
  public String getGroup() {
    return this.recipe.getGroup();
  }

  @Override
  public ResourceLocation getId() {
    return this.recipe.getId();
  }

  @Override
  public IRecipeSerializer<?> getSerializer() {
    return GradientRecipeSerializers.SHAPED;
  }

  @Override
  public IRecipeType<? extends IRecipe> getType() {
    return VanillaRecipeTypes.CRAFTING;
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.recipe.getRecipeOutput();
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.recipe.getIngredients();
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return this.recipe.canFit(width, height);
  }

  @Override
  public boolean matches(final IInventory inv, final World world) {
    return AgeUtils.playerMeetsAgeRequirement((CraftingInventory)inv, this.age) && this.recipe.matches(inv, world);
  }

  @Override
  public ItemStack getCraftingResult(final IInventory inv) {
    return this.recipe.getCraftingResult(inv);
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final IInventory inv) {
    final NonNullList<ItemStack> list = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

    for(int i = 0; i < list.size(); ++i) {
      final ItemStack stack = inv.getStackInSlot(i);

      if(stack.getItem() instanceof ToolItem) {
        stack.attemptDamageItem(1, rand, null);

        if(stack.isDamageable() && stack.getDamage() > stack.getMaxDamage()) {
          list.set(i, ItemStack.EMPTY);
        } else {
          list.set(i, stack.copy());
        }
      } else {
        list.set(i, ForgeHooks.getContainerItem(stack));
      }
    }

    return list;
  }

  @Override
  public int getRecipeWidth() {
    return this.recipe.getRecipeWidth();
  }

  @Override
  public int getRecipeHeight() {
    return this.recipe.getRecipeHeight();
  }

  @Override
  public Age getAge() {
    return this.age;
  }

  public static final class Serializer implements IRecipeSerializer<AgeGatedShapedToolRecipe> {
    private static final ResourceLocation id = GradientMod.resource("shaped");

    @Override
    public AgeGatedShapedToolRecipe read(final ResourceLocation recipeId, final JsonObject json) {
      final ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json);
      final Age age = Age.get(JSONUtils.getInt(json, "age", 1));

      return new AgeGatedShapedToolRecipe(recipe, age);
    }

    @Override
    public AgeGatedShapedToolRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      final ShapedRecipe recipe = RecipeSerializers.CRAFTING_SHAPED.read(recipeId, buffer);
      final Age age = Age.get(buffer.readVarInt());

      return new AgeGatedShapedToolRecipe(recipe, age);
    }

    @Override
    public void write(final PacketBuffer buffer, final AgeGatedShapedToolRecipe recipe) {
      RecipeSerializers.CRAFTING_SHAPED.write(buffer, recipe.recipe);
      buffer.writeVarInt(recipe.age.value());
    }

    @Override
    public ResourceLocation getName() {
      return id;
    }
  }
}
