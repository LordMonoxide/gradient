package lordmonoxide.gradient.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoneHammer extends GradientItemWorldTool {
  private final Set<ToolType> toolTypes = new HashSet<>();

  public StoneHammer() {
    super("stone_hammer", 0.5f, -2.4f, 2, 2, new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(20));
    this.toolTypes.add(ToolType.PICKAXE);
    this.toolTypes.add(GradientToolTypes.HAMMER);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, world, tooltip, flagIn);
    tooltip.add(new TextComponentTranslation("item.stone_hammer.tooltip"));
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return this.toolTypes;
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return 2;
  }
}
