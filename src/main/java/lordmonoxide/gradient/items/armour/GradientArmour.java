package lordmonoxide.gradient.items.armour;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class GradientArmour extends ItemArmor {
  public GradientArmour(final String name, final ArmorMaterial material, final int renderIndex, final EntityEquipmentSlot equipmentSlot) {
    super(material, renderIndex, equipmentSlot);
    this.setTranslationKey(name);
    this.setRegistryName(name);
  }
}
