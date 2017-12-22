package lordmonoxide.gradient.blocks.bronzefurnace;

import lordmonoxide.gradient.blocks.claycrucible.TileClayCrucible;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFurnaceInput;
import lordmonoxide.gradient.containers.SlotMetal;
import lordmonoxide.gradient.containers.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBronzeFurnace extends GradientContainer {
  private static final int INPUT_SLOTS_X = 13;
  private static final int INPUT_SLOTS_Y = 34;
  
  public ContainerBronzeFurnace(final InventoryPlayer inventory, final TileBronzeFurnace furnace) {
    super(furnace);
    
    for(int i = 0; i < TileBronzeFurnace.INPUT_SLOTS_COUNT; i++) {
      final int i2 = i;
      this.addSlotToContainer(new SlotMetal(this.inventory, TileClayCrucible.FIRST_METAL_SLOT + i, INPUT_SLOTS_X + (SLOT_X_SPACING + 8) * (i % 3), INPUT_SLOTS_Y + (SLOT_Y_SPACING + 2) * (i / 3)) {
        @Override public void onSlotChanged() {
          furnace.markDirty();
        }
        @Override public boolean canTakeStack(final EntityPlayer player) { return !furnace.isMelting(i2); }
      });
    }
    
    this.addPlayerSlots(inventory);
  }
}
