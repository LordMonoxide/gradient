package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FireStarter extends GradientItem implements GradientItemCraftable {
  public FireStarter() {
    super("fire_starter", CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.setMaxDamage(4);
    this.setCreativeTab(CreativeTabs.TOOLS);
  }
  
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    pos = pos.offset(facing);
    
    if(!playerIn.canPlayerEdit(pos, facing, stack)) {
      return EnumActionResult.FAIL;
    }
    
    if(worldIn.isAirBlock(pos)) {
      worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4f + 0.8f);
    }
    
    stack.damageItem(1, playerIn);
    return EnumActionResult.SUCCESS;
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(
      new ItemStack(this),
      GradientItems.FIBRE,
      Items.STICK,
      Items.STICK
    );
  }
}
