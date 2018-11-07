package lordmonoxide.gradient.blocks.firepit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFirePit extends Container {
  @Override
  public boolean canInteractWith(final EntityPlayer player) {
    return true;
  }
}
