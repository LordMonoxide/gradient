package lordmonoxide.gradient.blocks.bronzeboiler;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketLightBoiler implements IMessage {
  public static void send(final BlockPos pos) {
    GradientNet.CHANNEL.sendToServer(new PacketLightBoiler(pos));
  }
  
  private BlockPos pos;
  
  public PacketLightBoiler() { }
  
  public PacketLightBoiler(final BlockPos pos) {
    this.pos = pos;
  }
  
  @Override
  public void fromBytes(final ByteBuf buf) {
    try {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    } catch(Exception e) {
      System.out.println("Invalid position in PacketLightFurnace");
      System.out.println(e);
      this.pos = BlockPos.ORIGIN;
    }
  }
  
  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.pos.getX());
    buf.writeInt(this.pos.getY());
    buf.writeInt(this.pos.getZ());
  }
  
  public static class Handler implements IMessageHandler<PacketLightBoiler, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketLightBoiler packet, final MessageContext ctx) {
      ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
        final TileEntity te = ctx.getServerHandler().player.world.getTileEntity(packet.pos);
        
        if(!(te instanceof TileBronzeBoiler)) {
          return;
        }
        
        ((TileBronzeBoiler)te).light();
      });
      
      return null;
    }
  }
}
