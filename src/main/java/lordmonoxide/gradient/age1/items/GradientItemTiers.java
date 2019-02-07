package lordmonoxide.gradient.age1.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

public enum GradientItemTiers implements IItemTier {
  //TODO: repair ingredient
  STONE(0, 19, 0.5f, 0.0f, 5, () -> Ingredient.fromItems());

  /** The level of material this tool can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = WOOD/GOLD) */
  private final int harvestLevel;
  /** The number of uses this material allows. (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32) */
  private final int maxUses;
  /** The strength of this tool material against blocks which it is effective against. */
  private final float efficiency;
  /** Damage versus entities. */
  private final float attackDamage;
  /** Defines the natural enchantability factor of the material. */
  private final int enchantability;
  private final LazyLoadBase<Ingredient> repairMaterial;

  GradientItemTiers(final int harvestLevel, final int maxUses, final float efficiency, final float attackDamage, final int enchantability, final Supplier<Ingredient> repairMaterial) {
    this.harvestLevel = harvestLevel;
    this.maxUses = maxUses;
    this.efficiency = efficiency;
    this.attackDamage = attackDamage;
    this.enchantability = enchantability;
    this.repairMaterial = new LazyLoadBase<>(repairMaterial);
  }

  @Override
  public int getMaxUses() {
    return this.maxUses;
  }

  @Override
  public float getEfficiency() {
    return this.efficiency;
  }

  @Override
  public float getAttackDamage() {
    return this.attackDamage;
  }

  @Override
  public int getHarvestLevel() {
    return this.harvestLevel;
  }

  @Override
  public int getEnchantability() {
    return this.enchantability;
  }

  @Override
  public Ingredient getRepairMaterial() {
    return this.repairMaterial.getValue();
  }
}
