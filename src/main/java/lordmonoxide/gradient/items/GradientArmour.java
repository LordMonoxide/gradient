package lordmonoxide.gradient.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemGroup;

public class GradientArmour extends ItemArmor {
  public GradientArmour(final IArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final Properties properties) {
    super(material, equipmentSlot, properties.group(ItemGroup.COMBAT));
  }
}
