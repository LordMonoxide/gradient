package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class IngredientIC2Factory implements IIngredientFactory {
  @Override
  public Ingredient parse(final JsonContext context, final JsonObject json) {
    final String name = JsonUtils.getString(json, "name");
    final String variant = JsonUtils.getString(json, "variant", "");

    final ItemStack stack = IC2Items.getItem(name, variant);

    if(stack == null || stack.isEmpty()) {
      if(variant.isEmpty()) {
        throw new JsonSyntaxException("No IC2 item " + name);
      }

      throw new JsonSyntaxException("No IC2 item " + name + ':' + variant);
    }

    return Ingredient.fromStacks(stack);
  }
}
