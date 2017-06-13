package lordmonoxide.gradient.items.armour;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class GradientArmour extends ItemArmor {
  public GradientArmour(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
    super(materialIn, renderIndexIn, equipmentSlotIn);
    this.setUnlocalizedName(name);
    this.setRegistryName(name);
  }
}
