package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFood;
import lordmonoxide.gradient.containers.SlotFuel;
import lordmonoxide.gradient.containers.SlotOutput;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFirePit extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 34;
  
  public static final int INPUT_SLOTS_X = 103;
  public static final int INPUT_SLOTS_Y =  34;
  
  public static final int OUTPUT_SLOTS_X = 141;
  public static final int OUTPUT_SLOTS_Y =  34;
  
  public ContainerFirePit(InventoryPlayer playerInv, TileFirePit firepit) {
    super(firepit);
    
    for(int i = 0; i < TileFirePit.FUEL_SLOTS_COUNT; i++) {
      addSlotToContainer(new SlotFuel(this.inventory, TileFirePit.FIRST_FUEL_SLOT + i, FUEL_SLOTS_X + (i % 3) * (SLOT_X_SPACING + 8), FUEL_SLOTS_Y + (i / 3) * (SLOT_Y_SPACING + 8)) {
        @Override public void onSlotChanged() {
          firepit.markDirty();
        }
      });
    }
    
    for(int i = 0; i < TileFirePit.INPUT_SLOTS_COUNT; i++) {
      addSlotToContainer(new SlotFood(this.inventory, TileFirePit.FIRST_INPUT_SLOT + i, INPUT_SLOTS_X + SLOT_X_SPACING * i, INPUT_SLOTS_Y) {
        @Override public void onSlotChanged() {
          firepit.markDirty();
        }
      });
    }
  
    for(int i = 0; i < TileFirePit.INPUT_SLOTS_COUNT; i++) {
      addSlotToContainer(new SlotOutput(this.inventory, TileFirePit.FIRST_OUTPUT_SLOT + i, OUTPUT_SLOTS_X + SLOT_X_SPACING * i, OUTPUT_SLOTS_Y) {
        @Override public void onSlotChanged() {
          firepit.markDirty();
        }
      });
    }
    
    this.addPlayerSlots(playerInv);
  }
}
