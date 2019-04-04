package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerClayCrucible extends GradientContainer {
  public static final int FUEL_SLOTS_X = 13;
  public static final int FUEL_SLOTS_Y = 34;

  public static final int METAL_SLOTS_X = 13;
  public static final int METAL_SLOTS_Y = 17;

  public final TileClayCrucible crucible;
  public final InventoryPlayer playerInv;

  public ContainerClayCrucible(final InventoryPlayer playerInv, final TileClayCrucible crucible) {
    super(crucible);

    this.crucible = crucible;
    this.playerInv = playerInv;

    for(int i = 0; i < TileClayCrucible.METAL_SLOTS_COUNT; i++) {
      final int i2 = i;

      this.addSlot(new SlotMetal(this.inventory, TileClayCrucible.FIRST_METAL_SLOT + i, METAL_SLOTS_X + (SLOT_X_SPACING + 8) * (i % 5), METAL_SLOTS_Y + (SLOT_Y_SPACING + 2) * (i / 5)) {
        @Override public void onSlotChanged() {
          crucible.markDirty();
        }
        @Override public boolean canTakeStack(final EntityPlayer player) { return !crucible.isMelting(i2); }
        @Override public boolean isItemValid(final ItemStack stack) {
          return crucible.tank.getFluidAmount() < crucible.tank.getCapacity() && super.isItemValid(stack);
        }
      });
    }

    this.addPlayerSlots(playerInv);
  }
}
