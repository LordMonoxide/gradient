package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.entity.player.PlayerInventory;

public class BronzeBoilerContainer extends GradientContainer {
  public final TileBronzeBoiler boiler;

  public BronzeBoilerContainer(final int id, final PlayerInventory playerInv) {
    super(GradientContainers.BRONZE_BOILER, id, playerInv);
    this.boiler = null;
  }

  public BronzeBoilerContainer(final int id, final PlayerInventory playerInv, final TileBronzeBoiler boiler) {
    super(GradientContainers.BRONZE_BOILER, id, playerInv, boiler);

    this.boiler = boiler;

    this.addPlayerSlots(playerInv);
  }
}
