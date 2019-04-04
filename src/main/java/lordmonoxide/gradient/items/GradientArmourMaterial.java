package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum GradientArmourMaterial implements IArmorMaterial {
  //TODO: need to add tag for hide and use it here
  HIDE(GradientMod.resource("hide").toString(), 3, new int[] {1, 1, 2, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f, () -> Ingredient.fromItems(GradientItems.HIDE_COW));

  /** Holds the 'base' maxDamage that each armorType have. */
  private static final int[] MAX_DAMAGE_ARRAY = {13, 15, 16, 11};
  private final String name;
  /**
   * Holds the maximum damage factor (each piece multiply this by it's own value) of the material, this is the item
   * damage (how much can absorb before breaks)
   */
  private final int maxDamageFactor;
  /**
   * Holds the damage reduction (each 1 points is half a shield on gui) of each piece of armor (helmet, plate, legs and
   * boots)
   */
  private final int[] damageReductionAmountArray;
  /** Return the enchantability factor of the material */
  private final int enchantability;
  private final SoundEvent soundEvent;
  private final float toughness;
  private final LazyLoadBase<Ingredient> repairMaterial;

  GradientArmourMaterial(final String name, final int maxDamageFactor, final int[] damageReduction, final int enchantability, final SoundEvent soundEvent, final float toughness, final Supplier<Ingredient> repairMaterial) {
    this.name = name;
    this.maxDamageFactor = maxDamageFactor;
    this.damageReductionAmountArray = damageReduction;
    this.enchantability = enchantability;
    this.soundEvent = soundEvent;
    this.toughness = toughness;
    this.repairMaterial = new LazyLoadBase<>(repairMaterial);
  }

  @Override
  public int getDurability(final EntityEquipmentSlot slot) {
    return MAX_DAMAGE_ARRAY[slot.getIndex()] * this.maxDamageFactor;
  }

  @Override
  public int getDamageReductionAmount(final EntityEquipmentSlot slot) {
    return this.damageReductionAmountArray[slot.getIndex()];
  }

  @Override
  public int getEnchantability() {
    return this.enchantability;
  }

  @Override
  public SoundEvent getSoundEvent() {
    return this.soundEvent;
  }

  @Override
  public Ingredient getRepairMaterial() {
    return this.repairMaterial.getValue();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public String getName() {
    return this.name;
  }

  @Override
  public float getToughness() {
    return this.toughness;
  }
}
