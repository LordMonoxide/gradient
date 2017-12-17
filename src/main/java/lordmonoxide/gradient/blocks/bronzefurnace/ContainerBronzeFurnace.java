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
    
    this.addSlotToContainer(new SlotFurnaceInput(this.inventory, TileBronzeFurnace.INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        furnace.markDirty();
      }
    });
    
    this.addSlotToContainer(new SlotOutput(this.inventory, TileBronzeFurnace.OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        super.onSlotChanged();
        furnace.markDirty();
      }
    });
    
    this.addPlayerSlots(inventory);
  }
}
