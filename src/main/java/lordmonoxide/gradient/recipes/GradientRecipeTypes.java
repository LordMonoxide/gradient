package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.crafting.IRecipeType;

public final class GradientRecipeTypes {
  private GradientRecipeTypes() { }

  public static final IRecipeType<DryingRecipe>    DRYING    = RecipeType.get(GradientMod.resource("drying"), DryingRecipe.class);
  public static final IRecipeType<FirePitRecipe>   FIREPIT   = RecipeType.get(GradientMod.resource("firepit"), FirePitRecipe.class);
  public static final IRecipeType<FuelRecipe>      FUEL      = RecipeType.get(GradientMod.resource("fuel"), FuelRecipe.class);
  public static final IRecipeType<GrindingRecipe>  GRINDING  = RecipeType.get(GradientMod.resource("grinding"), GrindingRecipe.class);
  public static final IRecipeType<HardeningRecipe> HARDENING = RecipeType.get(GradientMod.resource("hardening"), HardeningRecipe.class);
  public static final IRecipeType<MixingRecipe>    MIXING    = RecipeType.get(GradientMod.resource("mixing"), MixingRecipe.class);
}
