package lordmonoxide.gradient.blocks.bronzefurnace;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFuel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeFurnace extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 24;

  public ContainerBronzeFurnace(final InventoryPlayer playerInv, final TileBronzeFurnace furnace) {
    super(furnace);

    for(int i = 0; i < TileBronzeFurnace.FUEL_SLOTS_COUNT; i++) {
      final int i2 = i;

      this.addSlotToContainer(new SlotFuel(this.inventory, TileBronzeFurnace.FIRST_FUEL_SLOT + i, FUEL_SLOTS_X + i % 3 * (SLOT_X_SPACING + 8), FUEL_SLOTS_Y + i / 3 * (SLOT_Y_SPACING + 2)) {
        @Override public void onSlotChanged() {
          furnace.markDirty();
        }
        @Override public boolean canTakeStack(final EntityPlayer player) { return !furnace.isBurning(i2); }
      });
    }

    this.addPlayerSlots(playerInv);
  }
}
