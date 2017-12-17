package lordmonoxide.gradient.blocks.bronzefurnace;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFurnaceInput;
import lordmonoxide.gradient.containers.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeFurnace extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;
  
  public ContainerBronzeFurnace(final InventoryPlayer inventory, final TileBronzeFurnace furnace) {
    super(furnace);
    
    for(int i = 0; i < TileBronzeFurnace.INPUT_SLOTS_COUNT; i++) {
      this.addSlotToContainer(new SlotFurnaceInput(this.inventory, TileBronzeFurnace.FIRST_INPUT_SLOT + i, INPUT_SLOTS_X + (i % 3) * (SLOT_X_SPACING + 8), INPUT_SLOTS_Y + (i / 3) * (SLOT_Y_SPACING + 8)) {
        @Override public void onSlotChanged() {
          super.onSlotChanged();
          furnace.markDirty();
        }
      });
    }
    
    for(int i = 0; i < TileBronzeFurnace.OUTPUT_SLOTS_COUNT; i++) {
      this.addSlotToContainer(new SlotOutput(this.inventory, TileBronzeFurnace.FIRST_OUTPUT_SLOT + i, OUTPUT_SLOTS_X + (i % 3) * (SLOT_X_SPACING + 8), OUTPUT_SLOTS_Y + (i / 3) * (SLOT_Y_SPACING + 8)) {
        @Override public void onSlotChanged() {
          super.onSlotChanged();
          furnace.markDirty();
        }
      });
    }
    
    this.addPlayerSlots(inventory);
  }
}
