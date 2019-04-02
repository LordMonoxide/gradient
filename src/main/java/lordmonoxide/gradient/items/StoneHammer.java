package lordmonoxide.gradient.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneHammer extends GradientItemWorldTool {
  public StoneHammer() {
    super("stone_hammer", 0.5f, -2.4f, 2, 2, 20);
    this.setHarvestLevel("pickaxe", 2);
    this.setHarvestLevel("hammer", 2);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(I18n.format("item.stone_hammer.tooltip"));
  }
}
