package lordmonoxide.gradient.science.geology;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.recipes.MeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

public final class Meltables {
  private Meltables() { }

  private static final Int2ObjectMap<Meltable> meltables = new Int2ObjectRBTreeMap<>();

  public static final Meltable INVALID_MELTABLE = new Meltable(0.0f, 0.0f, "water", 0);

  public static void registerMeltables() {
    GradientMod.logger.info("Registering meltables");

    //TODO: event
    final IForgeRegistry<IRecipe> registry = ForgeRegistries.RECIPES;

    for(final Ore.Metal ore : Ores.metals()) {
      final String uc = StringUtils.capitalize(ore.name);

      registry.register(
        new MeltingRecipe(GradientMod.MODID, ore.basic.meltTime, ore.basic.meltTemp, new FluidStack(ore.basic.getFluid(), Fluid.BUCKET_VOLUME), new OreIngredient("ore" + uc)).setRegistryName(GradientMod.resource("melting/ore_" + ore.name))
      );
    }

    for(final Metal metal : Metals.all()) {
      final String uc = StringUtils.capitalize(metal.name);

      registry.registerAll(
        new MeltingRecipe(GradientMod.MODID, metal.meltTime * GradientCasts.INGOT.amountForMetal(metal) / Fluid.BUCKET_VOLUME, metal.meltTemp, new FluidStack(metal.getFluid(), GradientCasts.INGOT.amountForMetal(metal)), new OreIngredient("ingot" + uc)).setRegistryName(GradientMod.resource("melting/ingot_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime / 4.0f, metal.meltTemp, new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME / 4), new OreIngredient("nugget" + uc)).setRegistryName(GradientMod.resource("melting/nugget_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime, metal.meltTemp, new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME), new OreIngredient("dust" + uc)).setRegistryName(GradientMod.resource("melting/dust_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime * GradientCasts.BLOCK.amountForMetal(metal) / Fluid.BUCKET_VOLUME, metal.meltTemp, new FluidStack(metal.getFluid(), GradientCasts.BLOCK.amountForMetal(metal)), new OreIngredient("block" + uc)).setRegistryName(GradientMod.resource("melting/block_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime, metal.meltTemp, new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME), new OreIngredient("crushed" + uc)).setRegistryName(GradientMod.resource("melting/crushed_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime, metal.meltTemp, new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME), new OreIngredient("purified" + uc)).setRegistryName(GradientMod.resource("melting/purified_" + metal.name)),
        new MeltingRecipe(GradientMod.MODID, metal.meltTime, metal.meltTemp, new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME), new OreIngredient("plate" + uc)).setRegistryName(GradientMod.resource("melting/plate_" + metal.name))
      );
    }
  }

  public static Meltable get(final ItemStack stack) {
    for(final int id : OreDictionary.getOreIDs(stack)) {
      final Meltable meltable = meltables.get(id);

      if(meltable != null) {
        return meltable;
      }
    }

    return INVALID_MELTABLE;
  }
}
