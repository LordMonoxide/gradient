package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class HardeningRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  private static final RecipeItemHelper recipeItemHelper = new RecipeItemHelper();
  private static final List<ItemStack> inputStacks = new ArrayList<>();

  private final String group;
  public final Age age;
  public final int ticks;
  public final boolean copyMeta;
  private final ItemStack output;
  private final NonNullList<Ingredient> input;
  private final boolean isSimple;

  public HardeningRecipe(final String group, final Age age, final int ticks, final boolean copyMeta, final ItemStack output, final Ingredient input) {
    this.group = group;
    this.age = age;
    this.ticks = ticks;
    this.copyMeta = copyMeta;
    this.output = output;
    this.input = NonNullList.from(Ingredient.EMPTY, input);
    this.isSimple = input.isSimple();
  }

  @Override
  public String getGroup() {
    return this.group;
  }

  @Override
  @Deprecated
  public boolean matches(final InventoryCrafting inv, final World world) {
    return false;
  }

  public boolean matches(final IBlockState state, final Age age) {
    if(age.ordinal() < this.age.ordinal()) {
      return false;
    }

    final Block block = state.getBlock();
    final ItemStack stack = new ItemStack(block, 1, block.damageDropped(state));

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
  public ItemStack getCraftingResult(final InventoryCrafting inv) {
    return this.output.copy();
  }

  public IBlockState getCraftingResult(final IBlockState current) {
    final int meta = this.copyMeta ? current.getBlock().getMetaFromState(current) : this.output.getMetadata();
    return Block.getBlockFromItem(this.output.getItem()).getStateFromMeta(meta);
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
}
