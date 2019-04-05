package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.common.crafting.RecipeType;

public final class GradientRecipeTypes {
  private GradientRecipeTypes() { }

  public static final RecipeType<AgeGatedShapedToolRecipe>    SHAPED    = RecipeType.get(GradientMod.resource("shaped"), AgeGatedShapedToolRecipe.class);
  public static final RecipeType<AgeGatedShapelessToolRecipe> SHAPELESS = RecipeType.get(GradientMod.resource("shapeless"), AgeGatedShapelessToolRecipe.class);
  public static final RecipeType<DryingRecipe>                DRYING    = RecipeType.get(GradientMod.resource("drying"), DryingRecipe.class);
  public static final RecipeType<FirePitRecipe>               FIREPIT   = RecipeType.get(GradientMod.resource("firepit"), FirePitRecipe.class);
  public static final RecipeType<FuelRecipe>                  FUEL      = RecipeType.get(GradientMod.resource("fuel"), FuelRecipe.class);
  public static final RecipeType<GrindingRecipe>              GRINDING  = RecipeType.get(GradientMod.resource("grinding"), GrindingRecipe.class);
  public static final RecipeType<HardeningRecipe>             HARDENING = RecipeType.get(GradientMod.resource("hardening"), HardeningRecipe.class);
  public static final RecipeType<MixingRecipe>                MIXING    = RecipeType.get(GradientMod.resource("mixing"), MixingRecipe.class);
}
