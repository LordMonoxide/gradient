package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class ClayCrucibleContainer extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 34;

  public static final int METAL_SLOTS_X = 13;
  public static final int METAL_SLOTS_Y = 17;

  public final TileClayCrucible crucible;

  public ClayCrucibleContainer(final int id, final PlayerInventory playerInv) {
    super(GradientContainers.CLAY_CRUCIBLE, id, playerInv);
    this.crucible = null;
  }

  public ClayCrucibleContainer(final int id, final PlayerInventory playerInv, final TileClayCrucible crucible) {
    super(GradientContainers.CLAY_CRUCIBLE, id, playerInv, crucible);

    this.crucible = crucible;

    for(int i = 0; i < TileClayCrucible.METAL_SLOTS_COUNT; i++) {
      final int i2 = i;

      this.addSlot(new SlotMetal(this.inventory, TileClayCrucible.FIRST_METAL_SLOT + i, METAL_SLOTS_X + (SLOT_X_SPACING + 8) * (i % 5), METAL_SLOTS_Y + (SLOT_Y_SPACING + 2) * (i / 5)) {
        @Override public void onSlotChanged() {
          crucible.markDirty();
        }
        @Override public boolean canTakeStack(final PlayerEntity player) { return !crucible.isMelting(i2); }
        @Override public boolean isItemValid(final ItemStack stack) {
          return crucible.tank.getFluidAmount() < crucible.tank.getCapacity() && super.isItemValid(stack);
        }
      });
    }

    this.addPlayerSlots(playerInv);
  }
}
