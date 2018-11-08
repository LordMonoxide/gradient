package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerFirePit extends Container {
  private Age playerAge = Age.AGE1;

  public void setPlayerAge(final Age age) {
    this.playerAge = age;
  }

  public Age getPlayerAge() {
    return this.playerAge;
  }

  @Override
  public boolean canInteractWith(final EntityPlayer player) {
    return true;
  }
}
