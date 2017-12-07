package lordmonoxide.gradient.recipes;

import com.sun.swing.internal.plaf.metal.resources.metal;
import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.items.CastItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class ExtraRecipes {
  private ExtraRecipes() { }
  
  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    System.out.println("Registering recipes...");
    
    registerAlloys();
    registerCasts(event);
  }
  
  private static void registerAlloys() {
    GradientMetals.alloys.forEach(alloy -> {
      final Ingredient[] ingredients = new Ingredient[alloy.inputs.size()];
      String recipeName = "recipe.alloy." + alloy.output.amount + "." + alloy.output.metal.name + ".from";
      
      for(int i = 0; i < alloy.inputs.size(); i++) {
        final GradientMetals.Metal input = alloy.inputs.get(i);
        ingredients[i] = new IngredientNBT(GradientMetals.getBucket(input));
        recipeName += "." + input.name;
      }
      
      System.out.println("Adding recipe " + recipeName);
      
      GameRegistry.addShapelessRecipe(
          new ResourceLocation(GradientMod.MODID, recipeName),
          null,
          GradientMetals.getBucket(alloy.output),
          ingredients
      );
    });
  }
  
  private static void registerCasts(final RegistryEvent.Register<IRecipe> event) {
    final IForgeRegistryModifiable registry = (IForgeRegistryModifiable)event.getRegistry();
    for(Object res : registry.getKeys()) {
      registry.remove((ResourceLocation)res);
    }
    
    //for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      //for(final GradientMetals.Metal metal : GradientMetals.metals) {
    final GradientCasts.Cast cast = GradientCasts.getCast("pickaxe");
    final GradientMetals.Metal metal = GradientMetals.getMetal("bronze");
    
        if(!cast.tool || metal.canMakeTools) {
          //final int amount = cast.amount / Fluid.BUCKET_VOLUME;
          
          //final Ingredient[] ingredients = new Ingredient[amount + 1];
          //ingredients[amount] = new IngredientNBT(ItemClayCast.getCast(cast));
          //Arrays.fill(ingredients, 0, amount, new IngredientNBT(GradientMetals.getBucket(metal)));
          
          //final ItemStack override = cast.itemOverride.get(metal);
          
          //final String recipeName = "cast." + cast.name + "." + metal.name;
          
          //System.out.println("Adding recipe " + recipeName);
          
          /*event.getRegistry().register(new ShapelessMetaAwareRecipe(
              override == null ? CastItem.getCastItem(cast, metal) : override,
              ingredients
          ).setRegistryName(GradientMod.MODID, recipeName));*/
          
          //ItemStack stack = override == null ? CastItem.getCastItem(cast, metal) : override;
          
          //System.out.println(stack);
          //System.out.println(stack.getTagCompound());
          
          // It's not losing NBT data, it's just matching the first recipe no matter what the NBT
          
          /*GameRegistry.addShapelessRecipe(
              new ResourceLocation(GradientMod.MODID, recipeName),
              null,
              override == null ? CastItem.getCastItem(cast, metal) : override,
              ingredients
          );*/
        }
      //}
    //}
    
    //IRecipe r1 = event.getRegistry().getValue(new ResourceLocation("gradient", "cast.pickaxe.bronze"));
    //IRecipe r2 = event.getRegistry().getValue(new ResourceLocation("gradient", "cast_item.pickaxe.bronze"));
  }
}
