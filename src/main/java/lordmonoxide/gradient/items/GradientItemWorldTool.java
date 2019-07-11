package lordmonoxide.gradient.items;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class GradientItemWorldTool extends Item {
  private final float harvestSpeed;
  private final float attackSpeed;
  private final int attackDamage;
  private final int attackDurabilityLost;

  public GradientItemWorldTool(final float harvestSpeed, final float attackSpeed, final int attackDamage, final int attackDurabilityLost, final Properties properties) {
    super(properties);
    this.harvestSpeed = harvestSpeed;
    this.attackSpeed  = attackSpeed;
    this.attackDamage = attackDamage;
    this.attackDurabilityLost = attackDurabilityLost;
  }

  @Override
  public boolean canHarvestBlock(final ItemStack stack, final BlockState state) {
    if(state.getBlockHardness(null, null) <= 1.0f) {
      return true;
    }

    for(final ToolType type : this.getToolTypes(stack)) {
      if(state.getBlock().isToolEffective(state, type)) {
        return true;
      }

      // Redstone has weird special-case handling
      if(type == ToolType.PICKAXE && state.getBlock() instanceof RedstoneOreBlock) {
        return true;
      }
    }

    return false;
  }

  @Override
  public float getDestroySpeed(final ItemStack stack, final BlockState state) {
    return this.canHarvestBlock(stack, state) ? this.harvestSpeed : 0.0f;
  }

  @Override
  public boolean hitEntity(final ItemStack stack, final LivingEntity target, final LivingEntity attacker) {
    stack.damageItem(this.attackDurabilityLost, attacker, e -> e.sendBreakAnimation(attacker.getActiveHand()));
    return true;
  }

  @Override
  public boolean onBlockDestroyed(final ItemStack stack, final World world, final BlockState state, final BlockPos pos, final LivingEntity entityLiving) {
    if(state.getBlockHardness(world, pos) != 0.0f) {
      stack.damageItem(1, entityLiving, e -> e.sendBreakAnimation(entityLiving.getActiveHand()));
    }

    return true;
  }

  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(final EquipmentSlotType equipmentSlot, final ItemStack stack) {
    final Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(equipmentSlot, stack);

    if(equipmentSlot == EquipmentSlotType.MAINHAND) {
      modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
      modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getAttackSpeed(stack), AttributeModifier.Operation.ADDITION));
    }

    return modifiers;
  }

  protected double getAttackDamage(final ItemStack stack) {
    return this.attackDamage;
  }

  protected double getAttackSpeed(final ItemStack stack) {
    return this.attackSpeed;
  }
}
