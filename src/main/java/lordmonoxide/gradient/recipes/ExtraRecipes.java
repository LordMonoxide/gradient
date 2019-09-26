package lordmonoxide.gradient.recipes;

import ic2.api.recipe.Recipes;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import lordmonoxide.gradient.utils.OreDictUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class ExtraRecipes {
  private ExtraRecipes() { }

  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    GradientMod.logger.info("Registering recipes...");

    final IForgeRegistry<IRecipe> registry = event.getRegistry();

    registerDusts(registry);
    registerPlates(registry);
    registerCasts(registry);
    registerTools(registry);
    registerNuggets(registry);
    registerMeltables(registry);

    if(Loader.isModLoaded("ic2")) {
      registerOreWashingRecipes();
      registerExtractorRecipes();
    }
  }

  private static void registerDusts(final IForgeRegistry<IRecipe> registry) {
    for(final Metal metal : Metals.all()) {
      if(metal.canMakeDustWithBasicGrinder) {
        final String recipeName = "recipe.dust." + metal.name;

        GradientMod.logger.info("Adding recipe {}", recipeName);

        registry.register(new GrindingRecipe(
          GradientMod.MODID,
          Age.AGE3,
          3,
          60,
          GradientItems.dust(metal).getItemStack(),
          new OreIngredient("ingot" + StringUtils.capitalize(metal.name))
        ).setRegistryName(GradientMod.resource(recipeName)));
      }
    }
  }

  private static void registerPlates(final IForgeRegistry<IRecipe> registry) {
    final ItemStack[] hammers = Metals.all().stream().filter(metal -> metal.canMakeTools).map(metal -> GradientItems.tool(GradientTools.HAMMER, metal).getWildcardItemStack()).toArray(ItemStack[]::new);

    for(final Metal metal : Metals.all()) {
      if(!metal.canMakeIngots || !metal.canMakePlates) {
        continue;
      }

      final String recipeName = "recipe.plate." + metal.name + ".hammered";

      GradientMod.logger.info("Adding recipe {}", recipeName);

      registry.register(new AgeGatedShapelessToolRecipe(
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.plate(metal).getItemStack(),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(hammers))
      ).setRegistryName(GradientMod.resource(recipeName)));
    }
  }

  private static void registerCasts(final IForgeRegistry<IRecipe> registry) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final Metal metal : Metals.all()) {
        if(cast.isValidForMetal(metal)) {
          final int amount = cast.amountForMetal(metal) / Fluid.BUCKET_VOLUME;

          final Ingredient[] ingredients = new Ingredient[amount + 1];
          ingredients[amount] = Ingredient.fromItem(GradientItems.clayCastHardened(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(Metals.getBucket(metal)));

          final String recipeName = "cast." + cast.name + '.' + metal.name;

          GradientMod.logger.info("Adding recipe {}", recipeName);

          registry.register(new ShapelessRecipes(
            GradientMod.MODID,
            GradientItems.castItem(cast, metal, 1),
            NonNullList.from(Ingredient.EMPTY, ingredients)
          ).setRegistryName(GradientMod.resource(recipeName)));
        }
      }
    }
  }

  private static void registerTools(final IForgeRegistry<IRecipe> registry) {
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final Metal metal : Metals.all()) {
        if(metal.canMakeTools) {
          registry.register(new ShapedRecipes(
            GradientMod.MODID,
            1, 3,
            NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(GradientItems.castItem(type.cast, metal, 1)), Ingredient.fromItem(GradientItems.LEATHER_CORD), Ingredient.fromItem(GradientItems.HARDENED_STICK)),
            GradientItems.tool(type, metal).getItemStack()
          ).setRegistryName(GradientMod.resource("tool." + type.cast.name + '.' + metal.name)));
        }
      }
    }
  }

  private static void registerNuggets(final IForgeRegistry<IRecipe> registry) {
    for(final Metal metal : Metals.all()) {
      if(!metal.canMakeIngots) {
        continue;
      }

      final ItemStack[] pickaxes = Metals.all().stream().filter(m -> m.canMakeTools && m.hardness >= metal.hardness).map(m -> GradientItems.tool(GradientTools.PICKAXE, m).getWildcardItemStack()).toArray(ItemStack[]::new);

      final String recipeName = "recipe.nugget." + metal.name + ".pickaxed";

      GradientMod.logger.info("Adding recipe {}", recipeName);

      registry.register(new AgeGatedShapelessToolRecipe(
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.nugget(metal).getItemStack(4),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(pickaxes))
      ).setRegistryName(GradientMod.resource(recipeName)));
    }
  }

  private static void registerMeltables(final IForgeRegistry<IRecipe> registry) {
    GradientMod.logger.info("Registering meltable recipes");

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

  @Optional.Method(modid = "ic2")
  private static void registerOreWashingRecipes() {
    for(final Ore.Metal ore : Ores.metals()) {
      final NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("amount", 1000); // Water amount

      final String oreName = StringUtils.capitalize(ore.name);

      Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushed" + oreName), nbt, false, OreDictUtils.getFirst("purified" + oreName));
    }
  }

  @Optional.Method(modid = "ic2")
  private static void registerExtractorRecipes() {
    Recipes.extractor.addRecipe(Recipes.inputFactory.forFluidContainer(FluidRegistry.WATER), null, false, GradientItems.SALT.getItemStack(4));
  }
}
