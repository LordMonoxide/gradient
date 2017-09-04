package lordmonoxide.gradient.items.armour;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ClothShirt extends GradientArmour {
  public ClothShirt() {
    super("cloth_shirt", GradientItems.MATERIAL_CLOTH, 0, EntityEquipmentSlot.CHEST);
  }
}
