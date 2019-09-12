package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneMattock extends GradientItemWorldTool {
  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4, 2, 50);
    this.setHarvestLevel("axe", 0);
    this.setHarvestLevel("shovel", 0);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).addInformation(stack, world, tooltip, flag);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final EntityPlayer player, final EntityLivingBase entity, final EnumHand hand) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).itemInteractionForEntity(itemstack, player, entity, hand);
  }
}
