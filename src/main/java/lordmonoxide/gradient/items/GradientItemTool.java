package lordmonoxide.gradient.items;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class GradientItemTool extends GradientItem {
  private final float harvestSpeed;
  private final float attackSpeed;
  private final int attackDamage;
  
  public GradientItemTool(String name, float harvestSpeed, float attackSpeed, int attackDamage) {
    super(name, CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.harvestSpeed = harvestSpeed;
    this.attackSpeed  = attackSpeed;
    this.attackDamage = attackDamage;
    
    this.setContainerItem(this);
  }
  
  // This causes the mattock to take damage every time it is used in a recipe
  @Override
  public ItemStack getContainerItem(ItemStack itemStack) {
    ItemStack stack = itemStack.copy();
    stack.attemptDamageItem(1, new Random());
    return stack;
  }
  
  public ItemStack getWildcardItemStack() {
    return this.getItemStack(1, OreDictionary.WILDCARD_VALUE);
  }
  
  @Override
  public boolean canHarvestBlock(IBlockState blockIn, ItemStack stack) {
    if(blockIn.getBlockHardness(null, null) <= 1.0f) {
      return true;
    }
    
    for(String type : this.getToolClasses(stack)) {
      if(blockIn.getBlock().isToolEffective(type, blockIn)) {
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    return this.canHarvestBlock(state, stack) ? this.harvestSpeed : 0.0f;
  }
  
  @Override
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    stack.damageItem(2, attacker);
    return true;
  }
  
  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
    if(state.getBlockHardness(worldIn, pos) != 0.0f) {
      stack.damageItem(1, entityLiving);
    }
    
    return true;
  }
  
  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(final EntityEquipmentSlot equipmentSlot, final ItemStack stack) {
    final Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(equipmentSlot, stack);
    
    if(equipmentSlot == EntityEquipmentSlot.MAINHAND) {
      modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage(stack), 0));
      modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getAttackSpeed(stack), 0));
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
