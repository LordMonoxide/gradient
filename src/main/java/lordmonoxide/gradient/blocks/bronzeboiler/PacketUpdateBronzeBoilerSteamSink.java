package lordmonoxide.gradient.blocks.bronzeboiler;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketUpdateBronzeBoilerSteamSink implements IMessage {
  public static void send(final BlockPos entityPos, final BlockPos updatePos) {
    GradientNet.CHANNEL.sendToAll(new PacketUpdateBronzeBoilerSteamSink(entityPos, updatePos));
  }

  private BlockPos entityPos;
  private BlockPos updatePos;

  public PacketUpdateBronzeBoilerSteamSink() { }

  public PacketUpdateBronzeBoilerSteamSink(final BlockPos entityPos, final BlockPos updatePos) {
    this.entityPos = entityPos;
    this.updatePos = updatePos;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    try {
      this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
      this.updatePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    } catch(final IndexOutOfBoundsException e) {
      System.out.println("Invalid position in PacketUpdateBronzeBoilerSteamSink");
      e.printStackTrace();
      this.entityPos = BlockPos.ORIGIN;
      this.updatePos = BlockPos.ORIGIN;
    }
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.entityPos.getX());
    buf.writeInt(this.entityPos.getY());
    buf.writeInt(this.entityPos.getZ());
    buf.writeInt(this.updatePos.getX());
    buf.writeInt(this.updatePos.getY());
    buf.writeInt(this.updatePos.getZ());
  }

  public static class Handler implements IMessageHandler<PacketUpdateBronzeBoilerSteamSink, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketUpdateBronzeBoilerSteamSink packet, final MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        final TileEntity te = Minecraft.getMinecraft().world.getTileEntity(packet.entityPos);

        if(!(te instanceof TileBronzeBoiler)) {
          return;
        }

        ((TileBronzeBoiler)te).updateOutput(packet.updatePos);
      });

      return null;
    }
  }
}
