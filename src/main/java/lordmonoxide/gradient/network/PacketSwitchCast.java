package lordmonoxide.gradient.network;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSwitchCast {
  public static void send(final GradientCasts.Cast cast) {
    GradientNet.CHANNEL.sendToServer(new PacketSwitchCast(cast));
  }

  private final GradientCasts.Cast cast;

  public PacketSwitchCast(final GradientCasts.Cast cast) {
    this.cast = cast;
  }

  public GradientCasts.Cast getCast() {
    return this.cast;
  }

  public static void encode(final PacketSwitchCast packet, final PacketBuffer buffer) {
    buffer.writeInt(packet.cast.id);
  }

  public static PacketSwitchCast decode(final PacketBuffer buffer) {
    try {
      return new PacketSwitchCast(GradientCasts.getCast(buffer.readInt()));
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.info("Invalid type in PacketSwitchCast", e);
      return new PacketSwitchCast(GradientCasts.PICKAXE);
    }
  }

  public static void handle(final PacketSwitchCast packet, final Supplier<NetworkEvent.Context> ctx) {
    if(packet.cast == null) {
      return;
    }

    ctx.get().enqueueWork(() -> {
      final InventoryPlayer inv = ctx.get().getSender().inventory;
      final ItemStack hand = inv.getCurrentItem();

      if(!(hand.getItem() instanceof ItemClayCastUnhardened)) {
        return;
      }

      inv.setInventorySlotContents(inv.currentItem, new ItemStack(GradientItems.CLAY_CAST_UNHARDENED.get(packet.cast), hand.getCount()));
    });
  }
}
