package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeOven;
import net.minecraft.entity.player.PlayerInventory;

public class BronzeOvenContainer extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;

  public final TileBronzeOven oven;

  public BronzeOvenContainer(final int id, final PlayerInventory playerInv) {
    super(GradientContainers.BRONZE_OVEN, id, playerInv);
    this.oven = null;
  }

  public BronzeOvenContainer(final int id, final PlayerInventory playerInv, final TileBronzeOven oven) {
    super(GradientContainers.BRONZE_OVEN, id, playerInv, oven);

    this.oven = oven;

    this.addSlot(new SlotFurnaceInput(this.inventory, TileBronzeOven.INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        oven.markDirty();
      }
    });

    this.addSlot(new SlotOutput(this.inventory, TileBronzeOven.OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        oven.markDirty();
      }
    });

    this.addPlayerSlots(playerInv);
  }
}
