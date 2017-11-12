package lordmonoxide.gradient.items;

import com.google.common.collect.Multimap;
import net.minecraft.block.BlockRedstoneOre;
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

public class GradientItemTool extends GradientItem {
  private final float harvestSpeed;
  private final float attackSpeed;
  private final int attackDamage;
  
  public GradientItemTool(final String name, final float harvestSpeed, final float attackSpeed, final int attackDamage) {
    super(name, CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.harvestSpeed = harvestSpeed;
    this.attackSpeed  = attackSpeed;
    this.attackDamage = attackDamage;
    
    this.setContainerItem(this);
  }
  
  public ItemStack getWildcardItemStack() {
    return this.getItemStack(1, OreDictionary.WILDCARD_VALUE);
  }
  
  @Override
  public boolean canHarvestBlock(final IBlockState blockIn, final ItemStack stack) {
    if(blockIn.getBlockHardness(null, null) <= 1.0f) {
      return true;
    }
    
    for(final String type : this.getToolClasses(stack)) {
      if(blockIn.getBlock().isToolEffective(type, blockIn)) {
        return true;
      }
      
      // Redstone has weird special-case handling
      if("pickaxe".equals(type) && blockIn.getBlock() instanceof BlockRedstoneOre) {
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
    return this.canHarvestBlock(state, stack) ? this.harvestSpeed : 0.0f;
  }
  
  @Override
  public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
    stack.damageItem(2, attacker);
    return true;
  }
  
  @Override
  public boolean onBlockDestroyed(final ItemStack stack, final World world, final IBlockState state, final BlockPos pos, final EntityLivingBase entityLiving) {
    if(state.getBlockHardness(world, pos) != 0.0f) {
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
