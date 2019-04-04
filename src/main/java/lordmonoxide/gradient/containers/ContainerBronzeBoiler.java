package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeBoiler extends GradientContainer {
  public final TileBronzeBoiler boiler;
  public final InventoryPlayer playerInv;

  public ContainerBronzeBoiler(final InventoryPlayer playerInv, final TileBronzeBoiler boiler) {
    super(boiler);

    this.boiler = boiler;
    this.playerInv = playerInv;

    this.addPlayerSlots(playerInv);
  }
}
