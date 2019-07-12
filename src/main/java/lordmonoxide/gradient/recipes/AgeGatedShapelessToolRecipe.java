package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Random;

public class AgeGatedShapelessToolRecipe implements IRecipe<CraftingInventory> {
  private static final Random rand = new Random();

  private final ShapelessRecipe recipe;
  public final Age age;

  public AgeGatedShapelessToolRecipe(final ShapelessRecipe recipe, final Age age) {
    this.recipe = recipe;
    this.age = age;
  }

  @Override
  public boolean matches(final CraftingInventory inv, final World world) {
    return AgeUtils.playerMeetsAgeRequirement(inv, this.age) && this.recipe.matches(inv, world);
  }

  @Override
  public ItemStack getCraftingResult(final CraftingInventory inv) {
    return this.recipe.getCraftingResult(inv);
  }

  @Override
  public boolean canFit(final int width, final int height) {
    return this.recipe.canFit(width, height);
  }

  @Override
  public ItemStack getRecipeOutput() {
    return this.recipe.getRecipeOutput();
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(final CraftingInventory inv) {
    final NonNullList<ItemStack> remaining = IRecipe.super.getRemainingItems(inv);

    for(int i = 0; i < remaining.size(); ++i) {
      final ItemStack stack = inv.getStackInSlot(i);

      if(stack.getItem() instanceof ToolItem) {
        stack.attemptDamageItem(1, rand, null);

        if(stack.isDamageable() && stack.getDamage() > stack.getMaxDamage()) {
          ForgeEventFactory.onPlayerDestroyItem(ForgeHooks.getCraftingPlayer(), stack, null);
          remaining.set(i, ItemStack.EMPTY);
        } else {
          remaining.set(i, stack.copy());
        }
      }
    }

    return remaining;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.recipe.getIngredients();
  }

  @Override
  public boolean isDynamic() {
    return this.recipe.isDynamic();
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
    return GradientRecipeSerializers.SHAPELESS;
  }

  @Override
  public IRecipeType<? extends IRecipe> getType() {
    return IRecipeType.CRAFTING;
  }

  public Age getAge() {
    return this.age;
  }

  public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AgeGatedShapelessToolRecipe> {
    @Override
    public AgeGatedShapelessToolRecipe read(final ResourceLocation recipeId, final JsonObject json) {
      final ShapelessRecipe recipe = IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, json);
      final Age age = Age.get(JSONUtils.getInt(json, "age", 1));

      return new AgeGatedShapelessToolRecipe(recipe, age);
    }

    @Override
    public AgeGatedShapelessToolRecipe read(final ResourceLocation recipeId, final PacketBuffer buffer) {
      final ShapelessRecipe recipe = IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, buffer);
      final Age age = Age.get(buffer.readVarInt());

      return new AgeGatedShapelessToolRecipe(recipe, age);
    }

    @Override
    public void write(final PacketBuffer buffer, final AgeGatedShapelessToolRecipe recipe) {
      IRecipeSerializer.CRAFTING_SHAPELESS.write(buffer, recipe.recipe);
      buffer.writeVarInt(recipe.age.value());
    }
  }
}
