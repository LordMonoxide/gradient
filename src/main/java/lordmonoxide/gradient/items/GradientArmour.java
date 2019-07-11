package lordmonoxide.gradient.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemGroup;

public class GradientArmour extends ArmorItem {
  public GradientArmour(final IArmorMaterial material, final EquipmentSlotType equipmentSlot, final Properties properties) {
    super(material, equipmentSlot, properties.group(ItemGroup.COMBAT));
  }
}
