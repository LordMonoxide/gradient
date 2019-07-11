package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import net.minecraft.entity.player.PlayerInventory;

public class BronzeGrinderContainer extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;

  public final TileBronzeGrinder grinder;

  public BronzeGrinderContainer(final int id, final PlayerInventory playerInv) {
    super(GradientContainers.BRONZE_GRINDER, id, playerInv);
    this.grinder = null;
  }

  public BronzeGrinderContainer(final int id, final PlayerInventory playerInv, final TileBronzeGrinder grinder) {
    super(GradientContainers.BRONZE_GRINDER, id, playerInv, grinder);

    this.grinder = grinder;

    this.addSlot(new SlotGrinderInput(this.inventory, TileBronzeGrinder.INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        grinder.markDirty();
      }
    });

    this.addSlot(new SlotOutput(this.inventory, TileBronzeGrinder.OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        grinder.markDirty();
      }
    });

    this.addPlayerSlots(playerInv);
  }
}
