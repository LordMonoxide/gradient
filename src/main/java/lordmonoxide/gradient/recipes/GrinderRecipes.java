package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GrinderRecipes {
  private GrinderRecipes() { }

  private static final Map<ItemStack, ItemStack> recipes = new HashMap<>();

  //TODO
/*
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

  public static boolean add(final ItemStack input, final ItemStack output) {
    return recipes.putIfAbsent(input, output) == null;
  }

  public static boolean add(final Item input, final ItemStack output) {
    return add(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), output);
  }

  public static boolean add(final Item input, final String output) {
    return add(input, OreDictUtils.getFirst(output));
  }

  public static boolean add(final String input, final ItemStack output) {
    boolean success = true;

    for(final ItemStack inputStack : OreDictionary.getOres(input)) {
      success = add(inputStack, output) && success;
    }

    return success;
  }

  public static boolean add(final String input, final String output) {
    return add(input, OreDictUtils.getFirst(output));
  }

  public static boolean remove(final ItemStack input) {
    return recipes.remove(input) != null;
  }
*/

  public static ItemStack getOutput(final ItemStack input) {
    //TODO
/*
    for(final Map.Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
      if(OreDictionary.itemMatches(entry.getKey(), input, false)) {
        return entry.getValue();
      }
    }
*/

    return ItemStack.EMPTY;
  }
}
