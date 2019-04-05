package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;

public final class GradientRecipeSerializers {
  private GradientRecipeSerializers() { }

  public static final IRecipeSerializer<AgeGatedShapedToolRecipe>    SHAPED    = RecipeSerializers.register(new AgeGatedShapedToolRecipe.Serializer());
  public static final IRecipeSerializer<AgeGatedShapelessToolRecipe> SHAPELESS = RecipeSerializers.register(new AgeGatedShapelessToolRecipe.Serializer());
  public static final IRecipeSerializer<DryingRecipe>                DRYING    = RecipeSerializers.register(new DryingRecipe.Serializer());
  public static final IRecipeSerializer<FirePitRecipe>               FIREPIT   = RecipeSerializers.register(new FirePitRecipe.Serializer());
  public static final IRecipeSerializer<FuelRecipe>                  FUEL      = RecipeSerializers.register(new FuelRecipe.Serializer());
  public static final IRecipeSerializer<GrindingRecipe>              GRINDING  = RecipeSerializers.register(new GrindingRecipe.Serializer());
  public static final IRecipeSerializer<HardeningRecipe>             HARDENING = RecipeSerializers.register(new HardeningRecipe.Serializer());
  public static final IRecipeSerializer<MixingRecipe>                MIXING    = RecipeSerializers.register(new MixingRecipe.Serializer());
}
