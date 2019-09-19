package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.utils.OreDictUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class GrinderRecipes {
  private GrinderRecipes() { }

  private static final Map<Ingredient, ItemStack> recipes = new HashMap<>();

  @SubscribeEvent
  public static void register(final RegistryEvent.Register<IRecipe> event) {
    add(Items.FLINT, "dustFlint");
    add("coal", "dustCoal");

    for(final Metal metal : Metals.all()) {
      final String name = StringUtils.capitalize(metal.name);

      final String crushedName = "crushed" + name;

      // Ores
      if(OreDictionary.doesOreNameExist(crushedName)) {
        add("ore" + name, crushedName);
      }

      final String dustName = "dust" + name;

      // Ingots
      if(OreDictionary.doesOreNameExist(dustName)) {
        add("ingot" + name, dustName);
      }
    }
  }

  public static boolean add(final Ingredient input, final ItemStack output) {
    return recipes.putIfAbsent(input, output) == null;
  }

  public static boolean add(final ItemStack input, final ItemStack output) {
    return add(Ingredient.fromStacks(input), output);
  }

  public static boolean add(final Item input, final ItemStack output) {
    return add(Ingredient.fromItem(input), output);
  }

  public static boolean add(final Item input, final String output) {
    return add(input, OreDictUtils.getFirst(output));
  }

  public static boolean add(final String input, final ItemStack output) {
    return add(new OreIngredient(input), output);
  }

  public static boolean add(final String input, final String output) {
    return add(input, OreDictUtils.getFirst(output));
  }

  public static ItemStack getOutput(final ItemStack input) {
    for(final Map.Entry<Ingredient, ItemStack> entry : recipes.entrySet()) {
      if(entry.getKey().apply(input)) {
        return entry.getValue();
      }
    }

    return ItemStack.EMPTY;
  }
}
