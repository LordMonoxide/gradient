package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ore;
import lordmonoxide.gradient.science.geology.Ores;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ExtraRecipes {
  private ExtraRecipes() { }

  //TODO
/*
  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    GradientMod.logger.info("Registering recipes...");

    final IForgeRegistry<IRecipe> registry = event.getRegistry();

    registerDusts(registry);
    registerPlates(registry);
    registerAlloys(registry);
    registerCasts(registry);
    registerTools(registry);
    registerNuggets(registry);
    registerOreWashingRecipes();
    registerExtractorRecipes();
  }

  private static void registerDusts(final IForgeRegistry<IRecipe> registry) {
    for(final Metal metal : Metals.all()) {
      if(metal.canMakeDustWithBasicGrinder) {
        final String recipeName = "recipe.dust." + metal.name;

        GradientMod.logger.info("Adding recipe {}", recipeName);

        registry.register(new GrindingRecipe(
<<<<<<< HEAD
          GradientMod.resource(recipeName),
=======
>>>>>>> master
          GradientMod.MODID,
          Age.AGE3,
          3,
          60,
          GradientItems.dust(metal).getItemStack(),
          NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)))
<<<<<<< HEAD
        ));
=======
        ).setRegistryName(GradientMod.resource(recipeName)));
>>>>>>> master
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
<<<<<<< HEAD
        GradientMod.resource(recipeName),
=======
>>>>>>> master
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.plate(metal).getItemStack(),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(hammers))
<<<<<<< HEAD
      ));
=======
      ).setRegistryName(GradientMod.resource(recipeName)));
>>>>>>> master
    }
  }

  private static void registerAlloys(final IForgeRegistry<IRecipe> registry) {
    registry.register(new ShapelessRecipes(
      GradientMod.MODID,
      new ItemStack(GradientItems.alloyNugget(Metals.BRONZE)),
      NonNullList.from(Ingredient.EMPTY, new OreIngredient("nuggetCopper"), new OreIngredient("nuggetCopper"), new OreIngredient("nuggetCopper"), new OreIngredient("nuggetTin"))
    ).setRegistryName(GradientMod.resource("recipe.alloy_nugget.bronze.1")));

    for(final GradientMetals.Alloy alloy : GradientMetals.alloys) {
      final ItemStack output = GradientItems.alloyNugget(alloy).getItemStack(alloy.output.amount);

      final Ingredient[] ingredients = new Ingredient[alloy.inputs.size()];

      final StringBuilder recipeName = new StringBuilder("recipe.alloy_nugget." + alloy.output.metal.name + '.' + alloy.output.amount);

      for(int i = 0; i < ingredients.length; i++) {
        ingredients[i] = new OreIngredient("nugget" + StringUtils.capitalize(alloy.inputs.get(i).name));
        recipeName.append('.').append(alloy.inputs.get(i).name);
      }

      GradientMod.logger.info("Adding recipe {}", recipeName);

      registry.register(new ShapelessRecipes(
<<<<<<< HEAD
        GradientMod.resource(recipeName.toString()),
        GradientMod.MODID,
        output,
        NonNullList.from(Ingredient.EMPTY, ingredients)
      ));
=======
        GradientMod.MODID,
        output,
        NonNullList.from(Ingredient.EMPTY, ingredients)
      ).setRegistryName(GradientMod.resource(recipeName.toString())));
>>>>>>> master
    }
  }

  private static void registerCasts(final IForgeRegistry<IRecipe> registry) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final Metal metal : Metals.all()) {
        if(cast.isValidForMetal(metal)) {
          final int amount = cast.amountForMetal(metal) / Fluid.BUCKET_VOLUME;

          final Ingredient[] ingredients = new Ingredient[amount + 1];
<<<<<<< HEAD
          ingredients[amount] = Ingredient.fromItems(GradientItems.CLAY_CAST_HARDENED.get(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(GradientMetals.getBucket(metal)));
=======
          ingredients[amount] = Ingredient.fromItem(GradientItems.clayCastHardened(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(Metals.getBucket(metal)));
>>>>>>> master

          final String recipeName = "cast." + cast.name + '.' + metal.name;

          GradientMod.logger.info("Adding recipe {}", recipeName);

<<<<<<< HEAD
          registry.register(new ShapelessRecipe(
            GradientMod.resource(recipeName),
            GradientMod.MODID,
            GradientItems.castItem(cast, metal, 1),
            NonNullList.from(Ingredient.EMPTY, ingredients)
          ));
=======
          registry.register(new ShapelessRecipes(
            GradientMod.MODID,
            GradientItems.castItem(cast, metal, 1),
            NonNullList.from(Ingredient.EMPTY, ingredients)
          ).setRegistryName(GradientMod.resource(recipeName)));
>>>>>>> master
        }
      }
    }
  }

  private static void registerTools(final IForgeRegistry<IRecipe> registry) {
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final Metal metal : Metals.all()) {
        if(metal.canMakeTools) {
<<<<<<< HEAD
          registry.register(new ShapedRecipe(
            GradientMod.resource("tool." + type.cast.name + '.' + metal.name),
            GradientMod.MODID,
            1, 3,
            NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(GradientItems.castItem(type.cast, metal, 1)), Ingredient.fromItems(GradientItems.LEATHER_CORD), Ingredient.fromItems(GradientItems.HARDENED_STICK)),
            GradientItems.tool(type, metal).getItemStack()
          ));
=======
          registry.register(new ShapedRecipes(
            GradientMod.MODID,
            1, 3,
            NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(GradientItems.castItem(type.cast, metal, 1)), Ingredient.fromItem(GradientItems.LEATHER_CORD), Ingredient.fromItem(GradientItems.HARDENED_STICK)),
            GradientItems.tool(type, metal).getItemStack()
          ).setRegistryName(GradientMod.resource("tool." + type.cast.name + '.' + metal.name)));
>>>>>>> master
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
<<<<<<< HEAD
        GradientMod.resource(recipeName),
=======
>>>>>>> master
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.nugget(metal).getItemStack(4),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(pickaxes))
<<<<<<< HEAD
      ));
=======
      ).setRegistryName(GradientMod.resource(recipeName)));
>>>>>>> master
    }
  }

  private static void registerOreWashingRecipes() {
<<<<<<< HEAD
    final NBTTagCompound nbt = new NBTTagCompound();
    nbt.putInt("amount", 1000); // Water amount
=======
    for(final Ore.Metal ore : Ores.metals()) {
      final NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("amount", 1000); // Water amount
>>>>>>> master

      final String oreName = StringUtils.capitalize(ore.name);
      Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushed" + oreName), nbt, false, OreDictUtils.getFirst("purified" + oreName));
    }
  }

  private static void registerExtractorRecipes() {
    Recipes.extractor.addRecipe(Recipes.inputFactory.forFluidContainer(FluidRegistry.WATER), null, false, GradientItems.SALT.getItemStack(4));
  }
*/
}
