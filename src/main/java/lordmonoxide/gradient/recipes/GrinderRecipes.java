package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class GrinderRecipes {
  private GrinderRecipes() { }

  private static final Map<ItemStack, ItemStack> recipes = new HashMap<>();

  @SubscribeEvent
  public static void register(final RegistryEvent.Register<IRecipe> event) {
    add(Items.FLINT, "dustFlint");
    add("coal", "dustCoal");
    add("sugarcane", OreDictHelper.getFirst("ingredientSugar", 2));
    add("cropWheat", "ingredientFlour");

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
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

  public static boolean add(final ItemStack input, final ItemStack output) {
    return recipes.putIfAbsent(input, output) == null;
  }

  public static boolean add(final Item input, final ItemStack output) {
    return add(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), output);
  }

  public static boolean add(final Item input, final String output) {
    return add(input, OreDictHelper.getFirst(output));
  }

  public static boolean add(final String input, final ItemStack output) {
    boolean success = true;

    for(final ItemStack inputStack : OreDictionary.getOres(input)) {
      success = add(inputStack, output) && success;
    }

    return success;
  }

  public static boolean add(final String input, final String output) {
    return add(input, OreDictHelper.getFirst(output));
  }

  public static boolean remove(final ItemStack input) {
    return recipes.remove(input) != null;
  }

  public static ItemStack getOutput(final ItemStack input) {
    for(final Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
      if(OreDictionary.itemMatches(entry.getKey(), input, false)) {
        return entry.getValue();
      }
    }

    return ItemStack.EMPTY;
  }
}
