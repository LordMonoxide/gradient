package lordmonoxide.gradient.inventory;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ContainerPlayer3x3Crafting extends PlayerContainer {
  private static final String[] ARMOR_SLOT_TEXTURES = {"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};
  private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = {EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

  private static final int CRAFT_SIZE = 3;

  public final List<Slot> craftingSlots = new ArrayList<>();
  public final List<Slot> invSlots = new ArrayList<>();

  public ContainerPlayer3x3Crafting(final PlayerInventory playerInventory, final boolean localWorld, final PlayerEntity player) {
    super(playerInventory, localWorld, player);

    this.field_75181_e = new CraftingInventory(this, CRAFT_SIZE, CRAFT_SIZE);

    this.addSlot(new CraftingResultSlot(playerInventory.player, this.field_75181_e, this.field_75179_f, 0, 154, 28));

    for(int x = 0; x < 2; ++x) {
      for(int y = 0; y < 2; ++y) {
        this.craftingSlots.add(this.addSlot(new Slot(this.field_75181_e, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18)));
      }
    }

    for(int i = 0; i < 4; ++i) {
      final EquipmentSlotType entityequipmentslot = VALID_EQUIPMENT_SLOTS[i];

      this.addSlot(new Slot(playerInventory, 36 + 3 - i, 8, 8 + i * 18) {
        @Override
        public int getSlotStackLimit() {
          return 1;
        }

        @Override
        public boolean isItemValid(final ItemStack stack) {
          return stack.canEquip(entityequipmentslot, player);
        }

        @Override
        public boolean canTakeStack(final PlayerEntity player) {
          final ItemStack itemstack = this.getStack();
          return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(player);
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public String getSlotTexture() {
          return ARMOR_SLOT_TEXTURES[entityequipmentslot.getIndex()];
        }
      });
    }

    for(int x = 0; x < 3; ++x) {
      for(int y = 0; y < 9; ++y) {
        this.invSlots.add(this.addSlot(new Slot(playerInventory, y + (x + 1) * 9, 8 + y * 18, 84 + x * 18)));
      }
    }

    for(int i = 0; i < 9; ++i) {
      this.invSlots.add(this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142)));
    }

    this.addSlot(new Slot(playerInventory, 40, 77, 62) {
      @Override
      @OnlyIn(Dist.CLIENT)
      public String getSlotTexture() {
        return "minecraft:item/empty_armor_slot_shield";
      }
    });

    for(int x = 0; x < CRAFT_SIZE; ++x) {
      for(int y = 0; y < CRAFT_SIZE; ++y) {
        if(x >= 2 || y >= 2) {
          this.craftingSlots.add(this.addSlot(new Slot(this.field_75181_e, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18)));
        }
      }
    }

    this.onCraftMatrixChanged(this.field_75181_e);
  }

  @Override
  public void onContainerClosed(final PlayerEntity player) {
    super.onContainerClosed(player);

    for(int i = 0; i < CRAFT_SIZE * CRAFT_SIZE; i++) {
      final ItemStack itemstack = this.field_75181_e.removeStackFromSlot(i);

      if(!itemstack.isEmpty()) {
        player.dropItem(itemstack, false);
      }
    }

    this.field_75179_f.clear();
  }
}
