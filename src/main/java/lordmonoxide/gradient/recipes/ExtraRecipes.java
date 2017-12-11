package lordmonoxide.gradient.recipes;

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
  }
  
  private static void registerDusts(final IForgeRegistry<IRecipe> registry) {
    Ingredient mortar = Ingredient.fromStacks(new ItemStack(GradientItems.MORTAR, 1, OreDictionary.WILDCARD_VALUE));
    
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
    ItemStack[] hammers = GradientMetals.metals.stream().map(metal -> Tool.getTool(GradientTools.HAMMER, metal, 1, OreDictionary.WILDCARD_VALUE)).toArray(ItemStack[]::new);
    
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
      final Ingredient[] ingredients = new Ingredient[alloy.inputs.size()];
      String recipeName = "recipe.alloy." + alloy.output.amount + "." + alloy.output.metal.name + ".from";
      
      for(int i = 0; i < alloy.inputs.size(); i++) {
        final GradientMetals.Metal input = alloy.inputs.get(i);
        ingredients[i] = new IngredientNBT(GradientMetals.getBucket(input));
        recipeName += "." + input.name;
      }
      
      System.out.println("Adding recipe " + recipeName);
      
      registry.register(new ShapelessRecipes(
          GradientMod.MODID,
          GradientMetals.getBucket(alloy.output),
          NonNullList.from(null, ingredients)
      ).setRegistryName(GradientMod.resource(recipeName)));
    });
  }
  
  private static void registerCasts(final IForgeRegistry<IRecipe> registry) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(!cast.tool || metal.canMakeTools) {
          final int amount = cast.amount / Fluid.BUCKET_VOLUME;
          
          final Ingredient[] ingredients = new Ingredient[amount + 1];
          ingredients[amount] = Ingredient.fromStacks(ItemClayCast.getCast(cast));
          Arrays.fill(ingredients, 0, amount, new IngredientNBT(GradientMetals.getBucket(metal)));
          
          final ItemStack override = cast.itemOverride.get(metal);
          
          final String recipeName = "cast." + cast.name + "." + metal.name;
          
          System.out.println("Adding recipe " + recipeName);
          
          registry.register(new ShapelessRecipes(
              GradientMod.MODID,
              override == null ? CastItem.getCastItem(cast, metal, 1) : override,
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
}
