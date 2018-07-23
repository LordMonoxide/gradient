package lordmonoxide.gradient.blocks.bronzefurnace;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketLightBronzeFurnace implements IMessage {
  public static void send(final BlockPos pos) {
    GradientNet.CHANNEL.sendToServer(new PacketLightBronzeFurnace(pos));
  }

  private BlockPos pos;

  public PacketLightBronzeFurnace() {

  }

  public PacketLightBronzeFurnace(final BlockPos pos) {
    this.pos = pos;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    try {
      this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.error("Invalid position in PacketLightBronzeFurnace", e);
      this.pos = BlockPos.ORIGIN;
    }
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.pos.getX());
    buf.writeInt(this.pos.getY());
    buf.writeInt(this.pos.getZ());
  }

  public static class Handler implements IMessageHandler<PacketLightBronzeFurnace, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketLightBronzeFurnace packet, final MessageContext ctx) {
      ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
        final TileEntity te = ctx.getServerHandler().player.world.getTileEntity(packet.pos);

        if(!(te instanceof TileBronzeFurnace)) {
          return;
        }

        ((TileBronzeFurnace)te).light();
      });

      return null;
    }
  }
}
