package lordmonoxide.gradient.blocks.manualgrinder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerManualGrinder extends Container {
  @Override
  public boolean canInteractWith(final EntityPlayer playerIn) {
    return true;
  }
}
