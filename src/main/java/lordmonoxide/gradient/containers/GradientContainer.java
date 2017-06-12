package lordmonoxide.gradient.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GradientContainer extends Container {
  public static final int SLOT_X_SPACING = 18;
  public static final int SLOT_Y_SPACING = 18;
  
  public static final int INV_SLOTS_X =   8;
  public static final int INV_SLOTS_Y =  84;
  public static final int HOT_SLOTS_Y = 142;
  
  protected final IItemHandler inventory;
  
  public GradientContainer(TileEntity te) {
    this.inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
  }
  
  protected void addPlayerSlots(InventoryPlayer invPlayer) {
    // Player inv
    for(int y = 0; y < 3; ++y) {
      for(int x = 0; x < 9; ++x) {
        this.addSlotToContainer(new Slot(invPlayer, x + y * 9 + 9, INV_SLOTS_X + x * SLOT_X_SPACING, INV_SLOTS_Y + y * SLOT_Y_SPACING));
      }
    }
    
    // Player hotbar
    for(int i = 0; i < 9; ++i) {
      this.addSlotToContainer(new Slot(invPlayer, i, INV_SLOTS_X + i * SLOT_X_SPACING, HOT_SLOTS_Y));
    }
  }
  
  @Override
  public boolean canInteractWith(EntityPlayer player) {
    return true;
  }
  
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int index) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.inventorySlots.get(index);
    
    if(slot != null && slot.getHasStack()) {
      ItemStack itemstack1 = slot.getStack();
      itemstack = itemstack1.copy();
      
      int containerSlots = this.inventorySlots.size() - player.inventory.mainInventory.size();
      
      if(index < containerSlots) {
        if(!this.mergeItemStack(itemstack1, containerSlots, this.inventorySlots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if(!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
        return ItemStack.EMPTY;
      }
      
      if(itemstack1.isEmpty()) {
        slot.putStack(ItemStack.EMPTY);
      } else {
        slot.onSlotChanged();
      }
      
      if(itemstack1.getCount() == itemstack.getCount()) {
        return ItemStack.EMPTY;
      }
      
      slot.onTake(player, itemstack1);
    }
    
    return itemstack;
  }
  
  /**
   * This is an exact copy-and-paste but fixes shift-clicking ignoring stack limits
   */
  @Override
  protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
    boolean flag = false;
    int i = startIndex;
    
    if(reverseDirection) {
      i = endIndex - 1;
    }
    
    if(stack.isStackable()) {
      while(!stack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex)) {
        Slot slot = this.inventorySlots.get(i);
        ItemStack itemstack = slot.getStack();
        
        if(areItemStacksEqual(stack, itemstack)) {
          int j = itemstack.getCount() + stack.getCount();
          int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
          
          if(j <= maxSize) {
            stack.setCount(0);
            itemstack.setCount(j);
            slot.onSlotChanged();
            flag = true;
          } else if(itemstack.getCount() < maxSize) {
            stack.setCount(stack.getCount() - (maxSize - itemstack.getCount()));
            itemstack.setCount(maxSize);
            slot.onSlotChanged();
            flag = true;
          }
        }
        
        if(reverseDirection) {
          --i;
        } else {
          ++i;
        }
      }
    }
    
    if(!stack.isEmpty()) {
      i = reverseDirection ? endIndex - 1 : startIndex;
      
      while(reverseDirection ? i >= startIndex : i < endIndex) {
        Slot slot1 = this.inventorySlots.get(i);
        ItemStack itemstack1 = slot1.getStack();
        
        if(itemstack1.isEmpty() && slot1.isItemValid(stack)) { // Forge: Make sure to respect isItemValid in the slot.
          slot1.putStack(stack.splitStack(slot1.getItemStackLimit(stack)));
          slot1.onSlotChanged();
          flag = true;
          
          if(stack.isEmpty()) {
            break;
          }
        }
        
        if(reverseDirection) {
          --i;
        } else {
          ++i;
        }
      }
    }
    
    return flag;
  }
  
  private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
    return stackB.getItem() == stackA.getItem() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB);
  }
}
