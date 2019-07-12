package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.containers.ClayCastContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ItemClayCastUnhardened extends BlockItem implements INamedContainerProvider {
  public final GradientCasts.Cast cast;

  public ItemClayCastUnhardened(final Block block, final GradientCasts.Cast cast, final Properties properties) {
    super(block, properties);
    this.cast = cast;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
    final ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);

    if(result.getType() != ActionResultType.SUCCESS) {
      if(!world.isRemote) {
        final BlockPos pos = player.getPosition();
        NetworkHooks.openGui((ServerPlayerEntity)player, this, pos);

        return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
      }
    }

    return result;
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.getName();
  }

  @Nullable
  @Override
  public Container createMenu(final int id, final PlayerInventory playerInv, final PlayerEntity player) {
    return new ClayCastContainer(id, playerInv);
  }
}
