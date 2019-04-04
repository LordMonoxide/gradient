package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.client.gui.GuiClayCast;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ItemClayCastUnhardened extends ItemBlock implements IInteractionObject {
  public final GradientCasts.Cast cast;

  public ItemClayCastUnhardened(final Block block, final GradientCasts.Cast cast, final Properties properties) {
    super(block, properties);
    this.cast = cast;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    final ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);

    if(result.getType() != EnumActionResult.SUCCESS) {
      if(!world.isRemote) {
        final BlockPos pos = player.getPosition();
        NetworkHooks.openGui((EntityPlayerMP)player, this, pos);

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
      }
    }

    return result;
  }

  @Override
  public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
    return new DummyContainer();
  }

  @Override
  public String getGuiID() {
    return GuiClayCast.ID.toString();
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Nullable
  @Override
  public ITextComponent getCustomName() {
    return null;
  }

  private static class DummyContainer extends Container {
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
      return true;
    }
  }
}
