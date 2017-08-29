package lordmonoxide.gradient.inventory;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ContainerPlayer3x3Crafting extends ContainerPlayer {
  private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
  
  private static final int CRAFT_SIZE = 3;
  
  public ContainerPlayer3x3Crafting(final InventoryPlayer playerInventory, final boolean localWorld, final EntityPlayer player) {
    super(playerInventory, localWorld, player);
    
    this.inventorySlots = Lists.newArrayList();
    this.craftMatrix = new InventoryCrafting(this, CRAFT_SIZE, CRAFT_SIZE);
    
    this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));
    
    for(int x = 0; x < 2; ++x) {
      for(int y = 0; y < 2; ++y) {
        this.addSlotToContainer(new Slot(this.craftMatrix, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18));
      }
    }
    
    for(int i = 0; i < 4; ++i) {
      final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[i];
      
      this.addSlotToContainer(new Slot(playerInventory, 36 + (3 - i), 8, 8 + i * 18) {
        @Override
        public int getSlotStackLimit() {
          return 1;
        }
        
        @Override
        public boolean isItemValid(@Nullable final ItemStack stack) {
          return stack != null && stack.getItem().isValidArmor(stack, entityequipmentslot, player);
        }
        
        @Override
        @Nullable
        @SideOnly(Side.CLIENT)
        public String getSlotTexture() {
          return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
        }
      });
    }
    
    for(int x = 0; x < 3; ++x) {
      for(int y = 0; y < 9; ++y) {
        this.addSlotToContainer(new Slot(playerInventory, y + (x + 1) * 9, 8 + y * 18, 84 + x * 18));
      }
    }
    
    for(int i = 0; i < 9; ++i) {
      this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
    }
    
    this.addSlotToContainer(new Slot(playerInventory, 40, 77, 62) {
      @Override
      @SideOnly(Side.CLIENT)
      public String getSlotTexture() {
        return "minecraft:items/empty_armor_slot_shield"; //$NON-NLS-1$
      }
    });
    
    for(int x = 0; x < CRAFT_SIZE; ++x) {
      for(int y = 0; y < CRAFT_SIZE; ++y) {
        if(x >= 2 || y >= 2) {
          this.addSlotToContainer(new Slot(this.craftMatrix, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18));
        }
      }
    }
    
    this.onCraftMatrixChanged(this.craftMatrix);
  }
  
  @Override
  public void onContainerClosed(final EntityPlayer player) {
    super.onContainerClosed(player);
    
    for(int i = 0; i < CRAFT_SIZE * CRAFT_SIZE; i++) {
      final ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
      
      if(!itemstack.isEmpty()) {
        player.dropItem(itemstack, false);
      }
    }
    
    this.craftResult.clear();
  }
}
