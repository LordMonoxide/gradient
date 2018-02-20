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
  private static final Map<ItemStack, ItemStack> recipes = new HashMap<>();

  @SubscribeEvent
  public static void register(final RegistryEvent.Register<IRecipe> event) {
    for(final String name : OreDictionary.getOreNames()) {
      System.out.println(name);

      for(final ItemStack stack : OreDictionary.getOres(name)) {
        System.out.println(stack);
      }
    }

    add(Items.FLINT, "dustFlint");
    add("coal", "dustCoal");

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String name = StringUtils.capitalize(metal.name);
      final String crushedName = "crushed" + name;
      final String dustName = "dust" + name;

      // Ores
      if(OreDictionary.doesOreNameExist(crushedName)) {
        add("ore" + name, crushedName);
      }

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
    return add(input, OreDictionary.getOres(output).iterator().next());
  }

  public static boolean add(final String input, final ItemStack output) {
    boolean success = true;

    for(final ItemStack inputStack : OreDictionary.getOres(input)) {
      success = add(inputStack, output) && success;
    }

    return success;
  }

  public static boolean add(final String input, final String output) {
    return add(input, OreDictionary.getOres(output).iterator().next());
  }

  public static boolean remove(final ItemStack input) {
    return recipes.remove(input) != null;
  }

  public static ItemStack getOutput(final ItemStack input) {
    for(Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
      if(OreDictionary.itemMatches(entry.getKey(), input, false)) {
        return entry.getValue();
      }
    }

    return ItemStack.EMPTY;
  }
}
