package lordmonoxide.gradient.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneMattock extends GradientItemWorldTool {
  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4, 20);
    this.setHarvestLevel("axe", 0);
    this.setHarvestLevel("shovel", 0);
  }
  
  @Override
  public EnumActionResult onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return Items.STONE_HOE.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
  }
}
