package lordmonoxide.gradient.blocks.bronzeboiler;

import lordmonoxide.gradient.containers.GradientContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeBoiler extends GradientContainer {
  public ContainerBronzeBoiler(final InventoryPlayer playerInv, final TileBronzeBoiler boiler) {
    super(boiler);

    this.addPlayerSlots(playerInv);
  }
}
