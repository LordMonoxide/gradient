package lordmonoxide.gradient.blocks.bronzeboiler;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFuel;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeBoiler extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 34;
  
  public ContainerBronzeBoiler(final InventoryPlayer playerInv, final TileBronzeBoiler boiler) {
    super(boiler);
    
    for(int i = 0; i < TileBronzeBoiler.FUEL_SLOTS_COUNT; i++) {
      this.addSlotToContainer(new SlotFuel(this.inventory, TileBronzeBoiler.FIRST_FUEL_SLOT + i, FUEL_SLOTS_X + (i % 3) * (SLOT_X_SPACING + 8), FUEL_SLOTS_Y + (i / 3) * (SLOT_Y_SPACING + 8)) {
        @Override public void onSlotChanged() {
          boiler.markDirty();
        }
      });
    }
    
    this.addPlayerSlots(playerInv);
  }
}
