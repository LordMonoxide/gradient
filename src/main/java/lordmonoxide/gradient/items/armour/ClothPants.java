package lordmonoxide.gradient.items.armour;

import lordmonoxide.gradient.items.GradientItemCraftable;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ClothPants extends GradientArmour implements GradientItemCraftable {
  public ClothPants() {
    super("cloth_pants", GradientItems.MATERIAL_CLOTH, 0, EntityEquipmentSlot.LEGS);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this.getDefaultInstance(),
      "CCC",
      "CNC",
      "CSC",
      'C', "cloth",
      'N', "needle",
      'S', "string"
    ));
  }
}
