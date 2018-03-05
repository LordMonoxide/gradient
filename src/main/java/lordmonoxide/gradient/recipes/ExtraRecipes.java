package lordmonoxide.gradient.recipes;

import ic2.api.recipe.Recipes;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.items.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class ExtraRecipes {
  private ExtraRecipes() { }
  
  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    System.out.println("Registering recipes...");

    final IForgeRegistry<IRecipe> registry = event.getRegistry();

    registerDusts(registry);
    registerPlates(registry);
    registerAlloys(registry);
    registerCasts(registry);
    registerTools(registry);
    registerOreWashingRecipes(registry);
  }
  
  private static void registerDusts(final IForgeRegistry<IRecipe> registry) {
    final Ingredient mortar = Ingredient.fromStacks(new ItemStack(GradientItems.MORTAR, 1, OreDictionary.WILDCARD_VALUE));
    
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(metal.canMakeDustWithMortar) {
        final String recipeName = "recipe.dust." + metal.name;
        
        System.out.println("Adding recipe " + recipeName);
        
        registry.register(new ShapelessToolRecipe(
            GradientMod.MODID,
            Dust.getDust(metal, 1),
            NonNullList.from(null, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), mortar)
        ).setRegistryName(GradientMod.resource(recipeName)));
      }
    }
  }
  
  private static void registerPlates(final IForgeRegistry<IRecipe> registry) {
    final ItemStack[] hammers = GradientMetals.metals.stream().map(metal -> Tool.getTool(GradientTools.HAMMER, metal, 1, OreDictionary.WILDCARD_VALUE)).toArray(ItemStack[]::new);
    
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(!metal.canMakePlates) {
        continue;
      }
      
      final String recipeName = "recipe.plate." + metal.name + ".hammered";
      
      System.out.println("Adding recipe " + recipeName);
      
      registry.register(new ShapelessToolRecipe(
          GradientMod.MODID,
          Plate.getPlate(metal, 1),
          NonNullList.from(null, new OreIngredient("ingot" + StringUtils.capitalize(metal.name)), Ingredient.fromStacks(hammers))
      ).setRegistryName(GradientMod.resource(recipeName)));
    }
  }
  
  private static void registerAlloys(final IForgeRegistry<IRecipe> registry) {
    GradientMetals.alloys.forEach(alloy -> {
      final String recipeName = "recipe.alloy." + alloy.output.amount + '.' + alloy.output.metal.name + ".from." + alloy.inputs.stream().map(metal -> metal.name).reduce((acc, name) -> acc + '.' + name).orElse("");
      
      System.out.println("Adding recipe " + recipeName);
      
      registry.register(new AlloyRecipe(
          GradientMod.MODID,
          alloy
      ).setRegistryName(GradientMod.resource(recipeName)));
    });
  }
  
  private static void registerCasts(final IForgeRegistry<IRecipe> registry) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(cast.isValidForMetal(metal)) {
          final int amount = cast.amountForMetal(metal) / Fluid.BUCKET_VOLUME;
          
          final Ingredient[] ingredients = new Ingredient[amount + 1];
          ingredients[amount] = Ingredient.fromStacks(ItemClayCast.getCast(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(GradientMetals.getBucket(metal)));
          
          final String recipeName = "cast." + cast.name + '.' + metal.name;
          
          System.out.println("Adding recipe " + recipeName);
          
          registry.register(new ShapelessRecipes(
              GradientMod.MODID,
              CastItem.getCastItem(cast, metal, 1),
              NonNullList.from(null, ingredients)
          ).setRegistryName(GradientMod.resource(recipeName)));
        }
      }
    }
  }
  
  private static void registerTools(final IForgeRegistry<IRecipe> registry) {
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          registry.register(new ShapedRecipes(
              GradientMod.MODID,
              1, 3,
              NonNullList.from(null, Ingredient.fromStacks(CastItem.getCastItem(type.cast, metal, 1)), new OreIngredient("string"), new OreIngredient("stickWood")),
              Tool.getTool(type, metal, 1, 0)
          ).setRegistryName(GradientMod.resource("tool." + type.cast.name + '.' + metal.name)));
        }
      }
    }
  }

  private static void registerOreWashingRecipes(final IForgeRegistry<IRecipe> registry) {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setInteger("amount", 1000); // Water amount

    Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushedBronze"), nbt, false, OreDictHelper.getFirst("crushedPurifiedBronze"));
    Recipes.oreWashing.addRecipe(Recipes.inputFactory.forOreDict("crushedMagnesium"), nbt, false, OreDictHelper.getFirst("crushedPurifiedMagnesium"));
  }
}
