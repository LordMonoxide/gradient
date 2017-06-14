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
import net.minecraftforge.oredict.ShapedOreRecipe;

public class StoneMattock extends GradientItemTool implements GradientItemCraftable {
  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4);
    this.setHarvestLevel("axe", 0);
    this.setHarvestLevel("shovel", 0);
    this.setMaxDamage(19);
  }
  
  @Override
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    return Items.STONE_HOE.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapedOreRecipe(
      this,
      "PFP",
      " S ",
      'P', GradientBlocks.PEBBLE,
      'F', "string",
      'S', "stickWood"
    ));
  }
}
