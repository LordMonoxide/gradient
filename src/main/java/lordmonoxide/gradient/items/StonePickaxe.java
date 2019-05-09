package lordmonoxide.gradient.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class StonePickaxe extends GradientItemWorldTool {
  private final Set<ToolType> toolTypes = new HashSet<>();

  public StonePickaxe() {
    super(0.5f, -2.4f, 4, 2, new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(50));
    this.toolTypes.add(ToolType.PICKAXE);
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

    return 0;
  }
}
