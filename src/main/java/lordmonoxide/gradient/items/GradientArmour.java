package lordmonoxide.gradient.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemArmor;

public class GradientArmour extends ItemArmor {
  public GradientArmour(final String name, final IArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final Properties properties) {
    super(material, equipmentSlot, properties);
    this.setRegistryName(name);
  }
}
