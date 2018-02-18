package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.SlotMetal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayCrucible extends GradientContainer {
  public static final int METAL_SLOTS_X = 13;
  public static final int METAL_SLOTS_Y = 17;
  
  public ContainerClayCrucible(final InventoryPlayer playerInv, final TileClayCrucible te) {
    super(te);
    
    for(int i = 0; i < TileClayCrucible.METAL_SLOTS_COUNT; i++) {
      final int i2 = i;
      
      this.addSlotToContainer(new SlotMetal(this.inventory, TileClayCrucible.FIRST_METAL_SLOT + i, METAL_SLOTS_X + (SLOT_X_SPACING + 8) * (i % 5), METAL_SLOTS_Y + (SLOT_Y_SPACING + 2) * (i / 5)) {
        @Override public void onSlotChanged() {
          te.markDirty();
        }
        @Override public boolean canTakeStack(final EntityPlayer player) { return !te.isMelting(i2); }
        @Override public boolean isItemValid(final ItemStack stack) {
          return te.tank.getFluidAmount() < te.tank.getCapacity() && super.isItemValid(stack);
        }
      });
    }
    
    this.addPlayerSlots(playerInv);
  }
}
