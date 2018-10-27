package lordmonoxide.gradient.items;

import net.minecraft.creativetab.CreativeTabs;

public class FireStarter extends GradientItem {
  public FireStarter() {
    super("fire_starter", CreativeTabs.TOOLS);
    this.maxStackSize = 1;
    this.setMaxDamage(4);
    this.setCreativeTab(CreativeTabs.TOOLS);
  }
}
