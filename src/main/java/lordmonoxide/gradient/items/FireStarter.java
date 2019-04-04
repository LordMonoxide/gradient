package lordmonoxide.gradient.items;

import net.minecraft.item.ItemGroup;

public class FireStarter extends GradientItem {
  public FireStarter() {
    super("fire_starter", new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(4));
  }
}
