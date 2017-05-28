package lordmonoxide.gradient.items;

import com.google.common.collect.Multimap;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StoneHammer extends GradientItemTool implements GradientItemCraftable {
  private final int damage;
  
  public StoneHammer() {
    super("stone_hammer", 0.5f);
    this.setHarvestLevel("pickaxe", 0);
    this.setHarvestLevel("hammer", 0);
    this.setMaxDamage(19);
    this.damage = 2;
  }
  
  @Override
  public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
    Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(equipmentSlot, stack);
    
    if(equipmentSlot == EntityEquipmentSlot.MAINHAND) {
      modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.damage, 0));
      modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
    }
    
    return modifiers;
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(
        new ItemStack(this),
        "P",
        "F",
        "S",
        Character.valueOf('P'), GradientBlocks.PEBBLE,
        Character.valueOf('F'), GradientItems.FIBRE,
        Character.valueOf('S'), Items.STICK
    );
  }
}
