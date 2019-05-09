package lordmonoxide.gradient.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class FireStarter extends Item {
  public FireStarter() {
    super(new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(4));
  }
}
