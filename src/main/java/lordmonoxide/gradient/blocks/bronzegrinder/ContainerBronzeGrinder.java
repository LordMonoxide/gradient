package lordmonoxide.gradient.blocks.bronzegrinder;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotGrinderInput;
import lordmonoxide.gradient.containers.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeGrinder extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;

  public ContainerBronzeGrinder(final InventoryPlayer inventory, final TileBronzeGrinder grinder) {
    super(grinder);

    this.addSlotToContainer(new SlotGrinderInput(this.inventory, TileBronzeGrinder.INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        grinder.markDirty();
      }
    });

    this.addSlotToContainer(new SlotOutput(this.inventory, TileBronzeGrinder.OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        grinder.markDirty();
      }
    });

    this.addPlayerSlots(inventory);
  }
}