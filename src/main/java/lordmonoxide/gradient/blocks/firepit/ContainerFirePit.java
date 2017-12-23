package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientFood;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotFood;
import lordmonoxide.gradient.containers.SlotFuel;
import lordmonoxide.gradient.containers.SlotOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerFirePit extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 34;
  
  public static final int INPUT_SLOTS_X = 103;
  public static final int INPUT_SLOTS_Y =  34;
  
  public static final int OUTPUT_SLOTS_X = 141;
  public static final int OUTPUT_SLOTS_Y =  34;
  
  public ContainerFirePit(final InventoryPlayer playerInv, final TileFirePit firePit) {
    super(firePit);
    
    for(int i = 0; i < TileFirePit.FUEL_SLOTS_COUNT; i++) {
      final int i2 = i;
      
      this.addSlotToContainer(new SlotFuel(this.inventory, TileFirePit.FIRST_FUEL_SLOT + i, FUEL_SLOTS_X + (i % 3) * (SLOT_X_SPACING + 8), FUEL_SLOTS_Y + (i / 3) * (SLOT_Y_SPACING + 8)) {
        @Override public void onSlotChanged() {
          firePit.markDirty();
        }
        @Override public boolean canTakeStack(final EntityPlayer player) { return !firePit.isBurning(i2); }
      });
    }
    
    this.addSlotToContainer(new SlotFood(this.inventory, TileFirePit.FIRST_INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        firePit.markDirty();
      }
      @Override public boolean isItemValid(final ItemStack stack) {
        return firePit.canOutputItem(GradientFood.get(stack).cooked);
      }
      @Override public boolean canTakeStack(final EntityPlayer player) { return !firePit.isCooking(); }
    });
    
    this.addSlotToContainer(new SlotOutput(this.inventory, TileFirePit.FIRST_OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y) {
      @Override public void onSlotChanged() {
        firePit.markDirty();
      }
    });
    
    this.addPlayerSlots(playerInv);
  }
}
