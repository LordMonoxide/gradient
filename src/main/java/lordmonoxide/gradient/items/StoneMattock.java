package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StoneMattock extends GradientItemTool implements GradientItemCraftable {
  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4);
    this.setHarvestLevel("axe", 0);
    this.setHarvestLevel("shovel", 0);
    this.setMaxDamage(19);
  }
  
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    return Items.STONE_HOE.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(
      new ItemStack(this),
      "   ",
      "PFP",
      " S ",
      Character.valueOf('P'), GradientBlocks.PEBBLE,
      Character.valueOf('F'), GradientItems.FIBRE,
      Character.valueOf('S'), Items.STICK
    );
  }
}
