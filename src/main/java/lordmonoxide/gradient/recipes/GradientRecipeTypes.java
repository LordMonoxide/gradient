package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.crafting.IRecipeType;

public final class GradientRecipeTypes {
  private GradientRecipeTypes() { }

  public static final IRecipeType<DryingRecipe> DRYING = IRecipeType.register(GradientMod.resource("drying").toString());
  public static final IRecipeType<FirePitRecipe> FIREPIT = IRecipeType.register(GradientMod.resource("firepit").toString());
  public static final IRecipeType<FuelRecipe> FUEL = IRecipeType.register(GradientMod.resource("fuel").toString());
  public static final IRecipeType<GrindingRecipe> GRINDING = IRecipeType.register(GradientMod.resource("grinding").toString());
  public static final IRecipeType<HardeningRecipe> HARDENING = IRecipeType.register(GradientMod.resource("hardening").toString());
  public static final IRecipeType<MixingRecipe> MIXING = IRecipeType.register(GradientMod.resource("mixing").toString());
}
