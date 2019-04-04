package lordmonoxide.gradient.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GradientItem extends Item {
  public GradientItem(final String name, final Properties properties) {
    super(properties);
    this.setRegistryName(name);
  }

  public ItemStack getItemStack(final int amount) {
    return new ItemStack(this, amount);
   }

  public ItemStack getItemStack() {
    return new ItemStack(this);
  }
}
