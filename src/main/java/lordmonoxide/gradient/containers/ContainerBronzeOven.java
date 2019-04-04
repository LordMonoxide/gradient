package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeOven;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeOven extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;

  public final TileBronzeOven oven;
  public final InventoryPlayer playerInv;

  public ContainerBronzeOven(final InventoryPlayer inventory, final TileBronzeOven oven) {
    super(oven);

    this.oven = oven;
    this.playerInv = inventory;

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

    this.addPlayerSlots(inventory);
  }
}
