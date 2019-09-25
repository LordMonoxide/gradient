package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBronzeFurnace extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 24;

  public ContainerBronzeFurnace(final InventoryPlayer playerInv, final TileBronzeFurnace furnace) {
    super(furnace);

    for(int slot = 0; slot < TileBronzeFurnace.FUEL_SLOTS_COUNT; slot++) {
      this.addSlotToContainer(new SlotItemHandler(this.inventory, TileBronzeFurnace.FIRST_FUEL_SLOT + slot, FUEL_SLOTS_X + slot % 3 * (SLOT_X_SPACING + 8), FUEL_SLOTS_Y + slot / 3 * (SLOT_Y_SPACING + 2)));
    }

    this.addPlayerSlots(playerInv);
  }
}
