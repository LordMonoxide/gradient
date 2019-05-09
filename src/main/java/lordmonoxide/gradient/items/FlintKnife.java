package lordmonoxide.gradient.items;

import net.minecraft.item.ItemGroup;

public class FlintKnife extends GradientItemWorldTool {
  public FlintKnife() {
    super(0, -1.0f, 3, 1, new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(50).group(ItemGroup.TOOLS));
  }
}
