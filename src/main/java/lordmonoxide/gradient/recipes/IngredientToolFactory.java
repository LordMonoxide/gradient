package lordmonoxide.gradient.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class IngredientToolFactory implements IIngredientFactory {
  @Nonnull
  @Override
  public Ingredient parse(JsonContext context, JsonObject json) {
    final String typeName = JsonUtils.getString(json, "head");
    final GradientTools.Type type = GradientTools.getType(typeName);
    
    final String metalName = JsonUtils.getString(json, "metal");
    
    if(metalName.equals("*")) {
      return new IngredientTool(type);
    }
    
    final GradientMetals.Metal metal = GradientMetals.getMetal(metalName);
    
    if(metal == GradientMetals.INVALID_METAL) {
      throw new JsonSyntaxException("Unknown metal '" + metalName + "'");
    }
    
    return new IngredientTool(type, metal);
  }
}
