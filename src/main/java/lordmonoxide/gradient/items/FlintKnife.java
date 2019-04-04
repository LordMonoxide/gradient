package lordmonoxide.gradient.items;

import net.minecraft.item.ItemGroup;

public class FlintKnife extends GradientItemWorldTool {
  public FlintKnife() {
    super("flint_knife", 0, -1.0f, 3, 1, new Properties().defaultMaxDamage(50).group(ItemGroup.TOOLS));
  }
}
