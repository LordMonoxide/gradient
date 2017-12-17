package lordmonoxide.gradient.blocks.claycast;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.GradientCasts;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketSwitchCast implements IMessage {
  public static void send(final GradientCasts.Cast cast) {
    GradientNet.CHANNEL.sendToServer(new PacketSwitchCast(cast));
  }
  
  private GradientCasts.Cast cast;
  
  public PacketSwitchCast() { }
  
  public PacketSwitchCast(final GradientCasts.Cast cast) {
    this.cast = cast;
  }
  
  public GradientCasts.Cast getCast() {
    return this.cast;
  }
  
  @Override
  public void fromBytes(final ByteBuf buf) {
    try {
      this.cast = GradientCasts.getCast(buf.readInt());
    } catch(final IndexOutOfBoundsException e) {
      System.out.println("Invalid type in PacketSwitchCast");
      e.printStackTrace();
      this.cast = GradientCasts.PICKAXE;
    }
  }
  
  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.cast.id);
  }
  
  public static class Handler implements IMessageHandler<PacketSwitchCast, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketSwitchCast packet, final MessageContext ctx) {
      if(packet.cast == null) {
        return null;
      }
      
      ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
        final ItemStack hand = ctx.getServerHandler().player.inventory.getCurrentItem();
        
        if(!(hand.getItem() instanceof ItemClayCastUnhardened)) {
          return;
        }
        
        hand.setItemDamage(packet.cast.id);
      });
      
      return null;
    }
  }
}
