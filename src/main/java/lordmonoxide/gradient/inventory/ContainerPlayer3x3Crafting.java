package lordmonoxide.gradient.inventory;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.recipes.GradientRecipeTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ContainerPlayer3x3Crafting extends ContainerPlayer {
  private static final String[] ARMOR_SLOT_TEXTURES = {"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};
  private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

  private static final int CRAFT_SIZE = 3;

  public final List<Slot> craftingSlots = new ArrayList<>();
  public final List<Slot> invSlots = new ArrayList<>();

  public ContainerPlayer3x3Crafting(final InventoryPlayer playerInventory, final boolean localWorld, final EntityPlayer player) {
    super(playerInventory, localWorld, player);

    this.inventorySlots = Lists.newArrayList();
    this.craftMatrix = new InventoryCrafting(this, CRAFT_SIZE, CRAFT_SIZE);

    this.addSlot(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 154, 28));

    for(int x = 0; x < 2; ++x) {
      for(int y = 0; y < 2; ++y) {
        this.craftingSlots.add(this.addSlot(new Slot(this.craftMatrix, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18)));
      }
    }

    for(int i = 0; i < 4; ++i) {
      final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[i];

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
        public boolean canTakeStack(final EntityPlayer player) {
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
        return "minecraft:items/empty_armor_slot_shield";
      }
    });

    for(int x = 0; x < CRAFT_SIZE; ++x) {
      for(int y = 0; y < CRAFT_SIZE; ++y) {
        if(x >= 2 || y >= 2) {
          this.craftingSlots.add(this.addSlot(new Slot(this.craftMatrix, y + x * CRAFT_SIZE, 98 + y * 18, 18 + x * 18)));
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

  @Override
  protected void slotChangedCraftingGrid(final World world, final EntityPlayer player, final IInventory craftMatrix, final InventoryCraftResult craftResult) {
    if(!world.isRemote) {
      ItemStack stack = this.getCraftingResult(world, craftMatrix, craftResult, (EntityPlayerMP)player, GradientRecipeTypes.SHAPED);

      if(stack == null) {
        stack = this.getCraftingResult(world, craftMatrix, craftResult, (EntityPlayerMP)player, GradientRecipeTypes.SHAPELESS);
      }

      if(stack != null) {
        craftResult.setInventorySlotContents(0, stack);
        ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetSlot(this.windowId, 0, stack));
        return;
      }
    }

    super.slotChangedCraftingGrid(world, player, craftMatrix, craftResult);
  }

  @Nullable
  private ItemStack getCraftingResult(final World world, final IInventory craftMatrix, final InventoryCraftResult craftResult, final EntityPlayerMP player, final RecipeType<? extends IRecipe> type) {
    final IRecipe irecipe = world.getServer().getRecipeManager().getRecipe(craftMatrix, world, type);

    if(craftResult.canUseRecipe(world, player, irecipe) && irecipe != null) {
      return irecipe.getCraftingResult(craftMatrix);
    }

    return null;
  }
}
