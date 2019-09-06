package lordmonoxide.gradient.recipes;

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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
          GradientMod.MODID,
          Age.AGE3,
          3,
          60,
          GradientItems.dust(metal).getItemStack(),
          NonNullList.from(Ingredient.EMPTY, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)))
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

  private static void registerAlloys(final IForgeRegistry<IRecipe> registry) {
    registry.register(new ShapelessRecipes(
      GradientMod.MODID,
      new ItemStack(GradientItems.alloyNugget(Metals.BRONZE)),
      NonNullList.from(Ingredient.EMPTY, new OreIngredient("nuggetCopper"), new OreIngredient("nuggetCopper"), new OreIngredient("nuggetCopper"), new OreIngredient("nuggetTin"))
    ).setRegistryName(GradientMod.resource("recipe.alloy_nugget.bronze.1")));

/* TODO
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
        GradientMod.MODID,
        output,
        NonNullList.from(Ingredient.EMPTY, ingredients)
      ).setRegistryName(GradientMod.resource(recipeName.toString())));
    }
*/
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

  //TODO ...omg
  private static void registerOreWashingRecipes() {
    final Class<?> ic2RecipesClass;
    try {
      ic2RecipesClass = Class.forName("ic2.api.recipe.Recipes");
    } catch(final ClassNotFoundException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - IC2 Recipes class not found", e);
      return;
    }

    final Class<?> ic2IRecipeInputClass;
    try {
      ic2IRecipeInputClass = Class.forName("ic2.api.recipe.IRecipeInput");
    } catch(final ClassNotFoundException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - IC2 IRecipeInput class not found", e);
      return;
    }

    final Field oreWashingField;
    try {
      oreWashingField = ic2RecipesClass.getDeclaredField("oreWashing");
    } catch(final NoSuchFieldException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - IC2 Recipes.oreWashing not found", e);
      return;
    }

    final Object oreWashing;
    try {
      oreWashing = oreWashingField.get(null);
    } catch(final IllegalAccessException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - could not get IC2 Recipes.oreWashing", e);
      return;
    }

    final Field inputFactoryField;
    try {
      inputFactoryField = ic2RecipesClass.getDeclaredField("inputFactory");
    } catch(final NoSuchFieldException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - IC2 Recipes.inputFactory not found", e);
      return;
    }

    final Object inputFactory;
    try {
      inputFactory = inputFactoryField.get(null);
    } catch(final IllegalAccessException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - could not get IC2 Recipes.inputFactory", e);
      return;
    }

    final Method forOreDictMethod;
    try {
      forOreDictMethod = inputFactory.getClass().getDeclaredMethod("forOreDict", String.class);
    } catch(final NoSuchMethodException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - could not find IC2 Recipes.inputFactory.forOreDict", e);
      return;
    }

    final Method addRecipeMethod;
    try {
      addRecipeMethod = oreWashing.getClass().getDeclaredMethod("addRecipe", ic2IRecipeInputClass, NBTTagCompound.class, boolean.class, ItemStack[].class);
    } catch(final NoSuchMethodException e) {
      GradientMod.logger.warn("Can't register ore washing recipe - could not find IC2 Recipes.oreWashing.addRecipe", e);
      return;
    }

    for(final Ore.Metal ore : Ores.metals()) {
      final NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("amount", 1000); // Water amount

      final String oreName = StringUtils.capitalize(ore.name);

      final Object input;
      try {
        input = forOreDictMethod.invoke(inputFactory, "crushed" + oreName);
      } catch(final IllegalAccessException | InvocationTargetException e) {
        GradientMod.logger.warn("Can't register ore washing recipe - could not invoke IC2 Recipes.inputFactory.forOreDict", e);
        return;
      }

      try {
        addRecipeMethod.invoke(oreWashing, input, nbt, false, new ItemStack[] {OreDictUtils.getFirst("purified" + oreName)});
      } catch(final IllegalAccessException | InvocationTargetException e) {
        GradientMod.logger.warn("Can't register ore washing recipe - could not invoke IC2 Recipes.oreWashing.addRecipe", e);
      }
    }
  }

  private static void registerExtractorRecipes() {
    final Class<?> ic2RecipesClass;
    try {
      ic2RecipesClass = Class.forName("ic2.api.recipe.Recipes");
    } catch(final ClassNotFoundException e) {
      GradientMod.logger.warn("Can't register extractor recipe - IC2 Recipes class not found", e);
      return;
    }

    final Class<?> ic2IRecipeInputClass;
    try {
      ic2IRecipeInputClass = Class.forName("ic2.api.recipe.IRecipeInput");
    } catch(final ClassNotFoundException e) {
      GradientMod.logger.warn("Can't register extractor recipe - IC2 IRecipeInput class not found", e);
      return;
    }

    final Field extractorField;
    try {
      extractorField = ic2RecipesClass.getDeclaredField("extractor");
    } catch(final NoSuchFieldException e) {
      GradientMod.logger.warn("Can't register extractor recipe - IC2 Recipes.extractor not found", e);
      return;
    }

    final Object extractor;
    try {
      extractor = extractorField.get(null);
    } catch(final IllegalAccessException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not get IC2 Recipes.extractor", e);
      return;
    }

    final Field inputFactoryField;
    try {
      inputFactoryField = ic2RecipesClass.getDeclaredField("inputFactory");
    } catch(final NoSuchFieldException e) {
      GradientMod.logger.warn("Can't register extractor recipe - IC2 Recipes.inputFactory not found", e);
      return;
    }

    final Object inputFactory;
    try {
      inputFactory = inputFactoryField.get(null);
    } catch(final IllegalAccessException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not get IC2 Recipes.inputFactory", e);
      return;
    }

    final Method forFluidContainerMethod;
    try {
      forFluidContainerMethod = inputFactory.getClass().getDeclaredMethod("forFluidContainer", Fluid.class);
    } catch(final NoSuchMethodException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not find IC2 Recipes.inputFactory.forFluidContainer", e);
      return;
    }

    final Method addRecipeMethod;
    try {
      addRecipeMethod = extractor.getClass().getDeclaredMethod("addRecipe", ic2IRecipeInputClass, NBTTagCompound.class, boolean.class, ItemStack[].class);
    } catch(final NoSuchMethodException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not find IC2 Recipes.extractor.addRecipe", e);
      return;
    }

    final Object input;
    try {
      input = forFluidContainerMethod.invoke(inputFactory, FluidRegistry.WATER);
    } catch(final IllegalAccessException | InvocationTargetException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not invoke IC2 Recipes.inputFactory.forFluidContainer", e);
      return;
    }

    try {
      addRecipeMethod.invoke(extractor, input, null, false, new ItemStack[] {GradientItems.SALT.getItemStack(4)});
    } catch(final IllegalAccessException | InvocationTargetException e) {
      GradientMod.logger.warn("Can't register extractor recipe - could not invoke IC2 Recipes.extractor.addRecipe", e);
    }
  }
}
