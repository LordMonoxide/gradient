package lordmonoxide.gradient.items.armour;

import lordmonoxide.gradient.items.GradientItemCraftable;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ClothShirt extends GradientArmour implements GradientItemCraftable {
  public ClothShirt() {
    super("cloth_shirt", GradientItems.MATERIAL_CLOTH, 0, EntityEquipmentSlot.CHEST);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
      "CNC",
      "CSC",
      "CCC",
      'C', "cloth",
      'N', "needle",
      'S', "string"
    ));
  }
}
