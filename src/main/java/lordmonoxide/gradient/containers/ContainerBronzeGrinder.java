package lordmonoxide.gradient.containers;

import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBronzeGrinder extends GradientContainer {
  public static final int INPUT_SLOTS_X = 13;
  public static final int INPUT_SLOTS_Y = 34;
  public static final int OUTPUT_SLOTS_X = 51;
  public static final int OUTPUT_SLOTS_Y = 34;

  public ContainerBronzeGrinder(final InventoryPlayer inventory, final TileBronzeGrinder grinder) {
    super(grinder);

    this.addSlotToContainer(new SlotItemHandler(this.inventory, TileBronzeGrinder.INPUT_SLOT, INPUT_SLOTS_X, INPUT_SLOTS_Y));
    this.addSlotToContainer(new SlotItemHandler(this.inventory, TileBronzeGrinder.OUTPUT_SLOT, OUTPUT_SLOTS_X, OUTPUT_SLOTS_Y));
    this.addPlayerSlots(inventory);
  }
}
