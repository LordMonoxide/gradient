package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IngredientIC2Factory implements IIngredientFactory {
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String name = JsonUtils.getString(json, "name");
    final String variant = JsonUtils.getString(json, "variant", "");

    final Class<?> ic2Items;
    try {
      ic2Items = Class.forName("ic2.api.item.IC2Items");
    } catch(final ClassNotFoundException e) {
      throw new JsonSyntaxException("Can't find IC2 items class", e);
    }

    final Method getItem;
    try {
      getItem = ic2Items.getDeclaredMethod("getItem", String.class, String.class);
    } catch(final NoSuchMethodException e) {
      throw new JsonSyntaxException("Can't find IC2 getItem method", e);
    }

    final ItemStack stack;
    try {
      stack = (ItemStack)getItem.invoke(null, name, variant);
    } catch(final IllegalAccessException | InvocationTargetException e) {
      throw new JsonSyntaxException("Failed to invoke IC2 getItem method", e);
    }

    if(stack == null || stack.isEmpty()) {
      if(variant.isEmpty()) {
        throw new JsonSyntaxException("No IC2 item " + name);
      }

      throw new JsonSyntaxException("No IC2 item " + name + ':' + variant);
    }

    return Ingredient.fromStacks(stack);
  }
}
