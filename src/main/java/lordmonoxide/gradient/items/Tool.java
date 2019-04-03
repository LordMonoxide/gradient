package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tool extends GradientItemWorldTool {
  public final GradientTools.Type type;
  public final GradientMetals.Metal metal;

  public Tool(final GradientTools.Type type, final GradientMetals.Metal metal) {
    super("tool." + type.cast.name + '.' + metal.name, metal.harvestSpeed, (float)(-4 + type.attackSpeed * metal.attackSpeedMultiplier), (int)(type.attackDamage * metal.attackDamageMultiplier), type.attackDurabilityLost, metal.durability);
    this.type = type;
    this.metal = metal;
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    this.type.tooltip(stack, world, tooltip);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return this.type.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  @Override
  public Set<String> getToolClasses(final ItemStack stack) {
    final Set<String> set = new HashSet<>();
    Collections.addAll(set, this.type.toolClass);
    return set;
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final String toolClass, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    if(Arrays.asList(this.type.toolClass).contains(toolClass)) {
      return this.metal.harvestLevel;
    }

    return -1;
  }
}
