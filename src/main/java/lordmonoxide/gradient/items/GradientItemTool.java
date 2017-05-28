package lordmonoxide.gradient.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GradientItemTool extends GradientItem {
  private float harvestSpeed;
  
  public GradientItemTool(String name, float baseSpeed) {
    super(name, CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.setHarvestSpeed(baseSpeed);
  }
  
  public void setHarvestSpeed(float harvestSpeed) {
    this.harvestSpeed = harvestSpeed;
  }
  
  @Override
  public boolean canHarvestBlock(IBlockState blockIn) {
    if(blockIn.getBlockHardness(null, null) <= 1.0f) {
      return true;
    }
    
    for(String type : this.getToolClasses(null)) {
      if(blockIn.getBlock().isToolEffective(type, blockIn)) {
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    return canHarvestBlock(state) ? this.harvestSpeed : 0.0f;
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
}
