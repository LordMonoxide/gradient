package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerClayCrucible extends GradientContainer {
  public static final int METAL_SLOTS_X = 13;
  public static final int METAL_SLOTS_Y = 17;

  public ContainerClayCrucible(final InventoryPlayer playerInv, final TileClayCrucible te) {
    super(te);

    for(int slot = 0; slot < TileClayCrucible.METAL_SLOTS_COUNT; slot++) {
      this.addSlotToContainer(new SlotItemHandler(this.inventory, TileClayCrucible.FIRST_METAL_SLOT + slot, ContainerClayCrucible.METAL_SLOTS_X + (GradientContainer.SLOT_X_SPACING + 8) * (slot % 5), ContainerClayCrucible.METAL_SLOTS_Y + (GradientContainer.SLOT_Y_SPACING + 2) * (slot / 5)));
    }

    this.addPlayerSlots(playerInv);
  }
}
