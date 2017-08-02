package lordmonoxide.gradient.blocks.claycast;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwitchCast implements IMessage {
  public static void send(GradientTools.Type type) {
    GradientNet.CHANNEL.sendToServer(new PacketSwitchCast(type));
  }
  
  private GradientTools.Type type;
  
  public PacketSwitchCast() { }
  
  public PacketSwitchCast(GradientTools.Type type) {
    this.type = type;
  }
  
  public GradientTools.Type getType() {
    return this.type;
  }
  
  @Override
  public void fromBytes(ByteBuf buf) {
    try {
      this.type = GradientTools.TYPES.get(buf.readInt());
    } catch(Exception e) {
      System.out.println("Invalid type in PacketSwitchCast");
      System.out.println(e);
      this.type = null;
    }
  }
  
  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.type.id);
  }
  
  public static class Handler implements IMessageHandler<PacketSwitchCast, IMessage> {
    @Override
    public IMessage onMessage(PacketSwitchCast packet, MessageContext ctx) {
      if(packet.type == null) {
        return null;
      }
      
      ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
        ItemStack hand = ctx.getServerHandler().player.inventory.getCurrentItem();
        
        if(!(hand.getItem() instanceof ItemClayCastUnhardened)) {
          return;
        }
        
        hand.setItemDamage(packet.type.id);
      });
      
      return null;
    }
  }
}
