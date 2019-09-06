package lordmonoxide.gradient.network;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.tileentities.TileClayMetalMixer;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketUpdateClayMetalMixerNeighbours implements IMessage {
  public static void send(final BlockPos entityPos) {
    GradientNet.CHANNEL.sendToAll(new PacketUpdateClayMetalMixerNeighbours(entityPos));
  }

  private BlockPos entityPos;

  public PacketUpdateClayMetalMixerNeighbours() { }

  public PacketUpdateClayMetalMixerNeighbours(final BlockPos entityPos) {
    this.entityPos = entityPos;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    try {
      this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.info("Invalid position in PacketUpdateClayMetalMixerNeighbours", e);
      this.entityPos = BlockPos.ORIGIN;
    }
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.entityPos.getX());
    buf.writeInt(this.entityPos.getY());
    buf.writeInt(this.entityPos.getZ());
  }

  public static class Handler implements IMessageHandler<PacketUpdateClayMetalMixerNeighbours, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketUpdateClayMetalMixerNeighbours packet, final MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        final TileClayMetalMixer te = WorldUtils.getTileEntity(Minecraft.getMinecraft().world, packet.entityPos, TileClayMetalMixer.class);

        if(te == null) {
          return;
        }

        te.updateAllSides();
        te.outputUpdated();
      });

      return null;
    }
  }
}
