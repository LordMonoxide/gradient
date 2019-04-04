package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.fml.common.Mod;

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
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(metal.canMakeDustWithMortar) {
        final String recipeName = "recipe.dust." + metal.name;

        GradientMod.logger.info("Adding recipe {}", recipeName);

        registry.register(new GrindingRecipe(
          GradientMod.resource(recipeName),
          GradientMod.MODID,
          Age.AGE3,
          3,
          60,
          GradientItems.dust(metal).getItemStack(),
          NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)))
        ));
      }
    }
  }

  private static void registerPlates(final IForgeRegistry<IRecipe> registry) {
    final ItemStack[] hammers = GradientMetals.metals.stream().filter(metal -> metal.canMakeTools).map(metal -> GradientItems.tool(GradientTools.HAMMER, metal).getWildcardItemStack()).toArray(ItemStack[]::new);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(!metal.canMakePlates) {
        continue;
      }

      final String recipeName = "recipe.plate." + metal.name + ".hammered";

      GradientMod.logger.info("Adding recipe {}", recipeName);

      registry.register(new AgeGatedShapelessToolRecipe(
        GradientMod.resource(recipeName),
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.plate(metal).getItemStack(),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(hammers))
      ));
    }
  }

  private static void registerAlloys(final IForgeRegistry<IRecipe> registry) {
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
        GradientMod.resource(recipeName.toString()),
        GradientMod.MODID,
        output,
        NonNullList.from(Ingredient.EMPTY, ingredients)
      ));
    }
  }

  private static void registerCasts(final IForgeRegistry<IRecipe> registry) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(cast.isValidForMetal(metal)) {
          final int amount = cast.amountForMetal(metal) / Fluid.BUCKET_VOLUME;

          final Ingredient[] ingredients = new Ingredient[amount + 1];
          ingredients[amount] = Ingredient.fromItems(GradientItems.CLAY_CAST_HARDENED.get(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(GradientMetals.getBucket(metal)));

          final String recipeName = "cast." + cast.name + '.' + metal.name;

          GradientMod.logger.info("Adding recipe {}", recipeName);

          registry.register(new ShapelessRecipe(
            GradientMod.resource(recipeName),
            GradientMod.MODID,
            GradientItems.castItem(cast, metal, 1),
            NonNullList.from(Ingredient.EMPTY, ingredients)
          ));
        }
      }
    }
  }

  private static void registerTools(final IForgeRegistry<IRecipe> registry) {
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          registry.register(new ShapedRecipe(
            GradientMod.resource("tool." + type.cast.name + '.' + metal.name),
            GradientMod.MODID,
            1, 3,
            NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(GradientItems.castItem(type.cast, metal, 1)), Ingredient.fromItems(GradientItems.LEATHER_CORD), Ingredient.fromItems(GradientItems.HARDENED_STICK)),
            GradientItems.tool(type, metal).getItemStack()
          ));
        }
      }
    }
  }

  private static void registerNuggets(final IForgeRegistry<IRecipe> registry) {
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(!metal.canMakeNuggets) {
        continue;
      }

      final ItemStack[] pickaxes = GradientMetals.metals.stream().filter(m -> m.canMakeTools && m.canMakeNuggets && m.hardness >= metal.hardness).map(m -> GradientItems.tool(GradientTools.PICKAXE, m).getWildcardItemStack()).toArray(ItemStack[]::new);

      final String recipeName = "recipe.nugget." + metal.name + ".pickaxed";

      GradientMod.logger.info("Adding recipe {}", recipeName);

      registry.register(new AgeGatedShapelessToolRecipe(
        GradientMod.resource(recipeName),
        GradientMod.MODID,
        Age.AGE3,
        GradientItems.nugget(metal).getItemStack(4),
        NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(pickaxes))
      ));
    }
  }

  private static void registerOreWashingRecipes() {
    final NBTTagCompound nbt = new NBTTagCompound();
    nbt.putInt("amount", 1000); // Water amount

    Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushedBronze"), nbt, false, OreDictUtils.getFirst("purifiedBronze"));
    Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushedMagnesium"), nbt, false, OreDictUtils.getFirst("purifiedMagnesium"));
  }

  private static void registerExtractorRecipes() {
    Recipes.extractor.addRecipe(Recipes.inputFactory.forFluidContainer(FluidRegistry.WATER), null, false, GradientItems.SALT.getItemStack(4));
  }
*/
}
