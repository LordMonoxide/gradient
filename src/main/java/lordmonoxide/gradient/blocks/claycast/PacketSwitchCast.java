package lordmonoxide.gradient.blocks.claycast;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSwitchCast implements IMessage {
  public static void send(GradientTools.Tool tool) {
    GradientNet.CHANNEL.sendToServer(new PacketSwitchCast(tool));
  }
  
  private GradientTools.Tool tool;
  
  public PacketSwitchCast() { }
  
  public PacketSwitchCast(GradientTools.Tool tool) {
    this.tool = tool;
  }
  
  public GradientTools.Tool getTool() {
    return this.tool;
  }
  
  @Override
  public void fromBytes(ByteBuf buf) {
    try {
      this.tool = GradientTools.tools.get(buf.readInt());
    } catch(Exception e) {
      System.out.println("Invalid tool in PacketSwitchCast");
      System.out.println(e);
      this.tool = null;
    }
  }
  
  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(this.tool.id);
  }
  
  public static class Handler implements IMessageHandler<PacketSwitchCast, IMessage> {
    @Override
    public IMessage onMessage(PacketSwitchCast packet, MessageContext ctx) {
      if(packet.tool == null) {
        return null;
      }
      
      ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
        ItemStack hand = ctx.getServerHandler().player.inventory.getCurrentItem();
        
        if(!(hand.getItem() instanceof ItemClayCastUnhardened)) {
          return;
        }
        
        hand.setItemDamage(packet.tool.id);
      });
      
      return null;
    }
  }
}
