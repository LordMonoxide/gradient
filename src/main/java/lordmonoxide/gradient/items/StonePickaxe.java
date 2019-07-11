package lordmonoxide.gradient.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final PlayerEntity player, @Nullable final BlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return 0;
  }
}
