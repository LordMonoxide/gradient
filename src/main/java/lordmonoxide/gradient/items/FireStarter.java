package lordmonoxide.gradient.items;

import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class FireStarter extends GradientItem implements GradientCraftable {
  public FireStarter() {
    super("fire_starter", CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.setMaxDamage(4);
    this.setCreativeTab(CreativeTabs.TOOLS);
  }
  
  public EnumActionResult onItemUse(final ItemStack stack, final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    BlockPos posFacing = pos.offset(facing);
    
    if(!player.canPlayerEdit(posFacing, facing, stack)) {
      return EnumActionResult.FAIL;
    }
    
    if(world.isAirBlock(posFacing)) {
      world.playSound(player, posFacing, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4f + 0.8f);
    }
    
    stack.damageItem(1, player);
    return EnumActionResult.SUCCESS;
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapelessOreRecipe(
      this,
      "string",
      "stickWood",
      "stickWood"
    ));
  }
}
